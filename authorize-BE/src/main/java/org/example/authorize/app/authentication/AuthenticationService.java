package org.example.authorize.app.authentication;

import lombok.RequiredArgsConstructor;
import org.example.authorize.security.jwt.AccessToken;
import org.example.authorize.security.jwt.TokenProvider;
import org.example.authorize.utils.generator.otp.OTPGenerator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * Authentication Service.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final TokenProvider tokenProvider;

    private final OTPGenerator<Instant> otpGenerator;

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


    public String generateOTP(String phone) {

        return phone;
    }
}
