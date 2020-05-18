package org.example.authorize.app.authentication;

import lombok.RequiredArgsConstructor;
import org.example.authorize.app.authentication.req.AuthReq;
import org.example.authorize.component.aspect.executiontime.LogExecutionTime;
import org.example.authorize.response.WResponseEntity;
import org.example.authorize.security.jwt.AccessToken;
import org.example.authorize.security.permission.PermissionGroup;
import org.example.authorize.utils.SecurityUtils;
import org.example.authorize.utils.constants.PermissionGroupConstants;
import org.example.authorize.utils.constants.URLConstants;
import org.example.authorize.component.version.APIVersion;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Authentication Controller.
 */
@APIVersion("1.0")
@RestController
@PermissionGroup(PermissionGroupConstants.AUTHENTICATION)
@RequestMapping(URLConstants.C_AUTHENTICATION)
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    /**
     * Authentication method.
     *
     * @param authReq request have username and password
     * @return return account token
     */
    @LogExecutionTime
    @PostMapping(URLConstants.M_AUTHENTICATION)
    public WResponseEntity<AccessToken> authorize(@RequestBody AuthReq authReq) {
        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(SecurityUtils.createUsernamePasswordAuthenticationToken(authReq));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return WResponseEntity.success(authenticationService.createAccessToken(authentication, authReq.isRememberMe()));
    }

    /**
     * Generate OTP value.
     *
     * @param authReq request data, it include phone number
     * @return return otp
     */
    @LogExecutionTime
    @PostMapping(URLConstants.M_OTP)
    public WResponseEntity<Boolean> generateOTP(@RequestBody AuthReq authReq) {
        return WResponseEntity.success(authenticationService.generateHOTP(authReq.getPhone()));
    }

    /**
     * Authentication by OTP.
     *
     * @param authReq request data, it include phone number and otp value
     * @return return access token if authentication successfully
     */
    @LogExecutionTime
    @PostMapping(URLConstants.M_OTP_VERIFY)
    public WResponseEntity<AccessToken> authorizeByOTP(@RequestBody AuthReq authReq) {
        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(SecurityUtils.createOTPAuthenticationToken(authReq));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return WResponseEntity.success(authenticationService.createAccessToken(authentication, true));
    }

    /**
     * Refresh Access Token.
     *
     * @param refreshReq refresh request, it have refresh token value.
     * @return return new access token
     */
    @LogExecutionTime
    @PostMapping(URLConstants.M_REFRESH_TOKEN)
    public WResponseEntity<AccessToken> refresh(@RequestBody RefreshReq refreshReq) {
        return WResponseEntity.success(authenticationService.refreshAccessToken(refreshReq.getToken()));
    }
}
