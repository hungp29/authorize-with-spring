package org.example.authorize.app.authentication;

import lombok.RequiredArgsConstructor;
import org.example.authorize.app.authmethod.AuthMethodService;
import org.example.authorize.entity.AuthMethod;
import org.example.authorize.enums.AuthType;
import org.example.authorize.security.jwt.AccessToken;
import org.example.authorize.security.jwt.TokenProvider;
import org.example.authorize.utils.OTPSupport;
import org.example.authorize.utils.sms.SMSSender;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Authentication Service.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final TokenProvider tokenProvider;

    private final AuthMethodService authMethodService;
    private final SMSSender smsSender;
    private final OTPSupport otpSupport;


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
    public boolean generateHOTP(String phone) {
        AuthMethod phoneAuthMethod = authMethodService.findByAuthTypeAndDetermineId(AuthType.PHONE_NUMBER, phone);
        if (null != phoneAuthMethod) {
            // Generate OTP by using HMAC One Time Password algorithm
            String otp = otpSupport.generateHOTP(phoneAuthMethod);

            // Prepare Data for sms
            Map<String, String> smsData = new HashMap<>();
            smsData.put("otp", otp);
            smsSender.sendSMS(phoneAuthMethod.getDetermineId(), "otp-message", smsData);
            return true;
        }
        return false;
    }
}
