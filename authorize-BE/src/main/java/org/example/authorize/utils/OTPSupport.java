package org.example.authorize.utils;

import lombok.RequiredArgsConstructor;
import org.example.authorize.app.authmethod.AuthMethodService;
import org.example.authorize.config.prop.OTPProperties;
import org.example.authorize.entity.AuthMethod;
import org.example.authorize.enums.AuthType;
import org.example.authorize.exception.AuthMethodException;
import org.example.authorize.utils.generator.otp.OTPGenerator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;

/**
 * OTP Support.
 */
@Component
@RequiredArgsConstructor
public class OTPSupport {

    private final OTPProperties otpProps;

    private final OTPGenerator<Long> hotpGenerator;
    private final OTPGenerator<Instant> totpGenerator;

    private final AuthMethodService authMethodService;

    /**
     * Generate OTP by using HMAC One Time Password algorithm.
     *
     * @param phone the phone number
     * @return return otp value is generated
     */
    public String generateHOTP(String phone) {
        AuthMethod phoneAuthMethod = authMethodService.findByAuthTypeAndAuthData1(AuthType.PHONE_NUMBER, phone);
        return generateHOTP(phoneAuthMethod);
    }

    /**
     * Generate OTP by using HMAC One Time Password algorithm.
     *
     * @param phoneAuthMethod the phone auth method instance
     * @return return otp value is generated
     */
    public String generateHOTP(AuthMethod phoneAuthMethod) {
        if (null != phoneAuthMethod) {
            // Re-generate secret key if it's empty
            if (StringUtils.isEmpty(phoneAuthMethod.getAuthData2())) {
                phoneAuthMethod = authMethodService.regenerateSecretKeyForPhoneAuthMethod(phoneAuthMethod);
            }

            // Increase moving factor
            long movingFactor = authMethodService.increaseMovingFactorForPhoneAuthMethod(phoneAuthMethod);
            // Generate OTP base on secret key and moving factor
            String otp = hotpGenerator.generate(CommonUtils.stringToByteArray(phoneAuthMethod.getAuthData2()),
                    movingFactor, otpProps.getOtpLength(), false, 20);
            // Set expiration time for phone auth method
            authMethodService.setExpirationForPhoneAuthMethod(phoneAuthMethod);

            return otp;
        }
        throw new AuthMethodException("Can't find PHONE_NUMBER auth method for phone");
    }

    /**
     * Verify OTP value is generated by HMAC One Time Password Algorithm.
     *
     * @param phone phone number
     * @param otp   otp value
     * @return return true if otp value is valid, otherwise return false
     */
    public boolean verifyHOTP(String phone, String otp) {
        AuthMethod phoneAuthMethod = authMethodService.findByAuthTypeAndAuthData1(AuthType.PHONE_NUMBER, phone);
        if (null != phoneAuthMethod && !StringUtils.isEmpty(phoneAuthMethod.getAuthData2())) {
            long movingFactor = authMethodService.getMovingFactorOfPhoneAuthMethod(phoneAuthMethod);
            // Generate OTP base on secret key and moving factor
            String otpGenerated = hotpGenerator.generate(CommonUtils.stringToByteArray(phoneAuthMethod.getAuthData2()),
                    movingFactor, otpProps.getOtpLength(), false, 20);

            return otpGenerated.equals(otp) &&
                    (null == phoneAuthMethod.getExpireDate() || !LocalDateTime.now().isAfter(phoneAuthMethod.getExpireDate()));
        }
        return false;
    }
}