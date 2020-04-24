package org.example.authorize.app.authentication;

import lombok.RequiredArgsConstructor;
import org.example.authorize.app.authmethod.AuthMethodService;
import org.example.authorize.config.prop.ApplicationProperties;
import org.example.authorize.entity.AuthMethod;
import org.example.authorize.enums.AuthType;
import org.example.authorize.security.jwt.AccessToken;
import org.example.authorize.security.jwt.TokenProvider;
import org.example.authorize.utils.CommonUtils;
import org.example.authorize.utils.generator.otp.OTPGenerator;
import org.example.authorize.utils.sms.SMSSender;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Authentication Service.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final TokenProvider tokenProvider;
    private final OTPGenerator<Long> hotpGenerator;
    private final ApplicationProperties appProps;

    private final AuthMethodService authMethodService;
    private final SMSSender smsSender;


    /**
     * Create access token by using method of Token Provider.
     *
     * @param authentication Authentication instance
     * @param rememberMe     remember me
     * @return return access token instance
     */
    public AccessToken createAccessToken(Authentication authentication, boolean rememberMe) {
        return tokenProvider.createAccessToken(authentication, rememberMe);
    }

    /**
     * Refresh access token by using refresh token.
     *
     * @param refreshToken the refresh token
     * @return return new access token
     */
    public AccessToken refreshAccessToken(String refreshToken) {
        return tokenProvider.refreshAccessToken(refreshToken);
    }

    /**
     * Generate OTP value.
     *
     * @param phone the phone number of account.
     * @return return value
     */
    public boolean generateOTP(String phone) {
        AuthMethod phoneAuthMethod = authMethodService.findByAuthTypeAndAuthData1(AuthType.PHONE_NUMBER, phone);
        if (null != phoneAuthMethod) {
            // Re-generate secret key if it's empty
            if (StringUtils.isEmpty(phoneAuthMethod.getAuthData2())) {
                phoneAuthMethod = authMethodService.regenerateSecretKeyForPhoneAuthMethod(phoneAuthMethod);
            }

            // Increase moving factor
            long movingFactor = authMethodService.increaseMovingFactorForPhoneAuthMethod(phoneAuthMethod);
            // Generate OTP base on secret key and moving factor
            String otp = hotpGenerator.generate(CommonUtils.stringToByteArray(phoneAuthMethod.getAuthData2()),
                    movingFactor, appProps.getOtpLength(), false, 20);

            // Prepare Data for sms
            Map<String, String> smsData = new HashMap<>();
            smsData.put("otp", otp);
            smsSender.sendSMS(phoneAuthMethod.getAuthData1(), "otp-message", smsData);
            return true;
        }
        return false;
    }
}
