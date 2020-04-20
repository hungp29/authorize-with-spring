package org.example.authorize.app.authentication;

import lombok.RequiredArgsConstructor;
import org.example.authorize.response.WResponseEntity;
import org.example.authorize.security.jwt.AccessToken;
import org.example.authorize.security.jwt.TokenProvider;
import org.example.authorize.security.permission.PermissionGroup;
import org.example.authorize.utils.SecurityUtils;
import org.example.authorize.utils.constants.PermissionGroupConstants;
import org.example.authorize.utils.constants.URLConstants;
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
@RestController
@PermissionGroup(PermissionGroupConstants.AUTHENTICATION)
@RequestMapping(URLConstants.C_AUTHENTICATION)
@RequiredArgsConstructor
public class AuthenticationController {

    private final TokenProvider tokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    /**
     * Authentication method.
     *
     * @return return account token
     */
    @PostMapping(URLConstants.M_AUTHENTICATION)
    public WResponseEntity<AccessToken> authorize(@RequestBody AuthReq authReq) {
        Authentication authentication = authenticationManagerBuilder.getObject().
                authenticate(SecurityUtils.createUsernamePasswordAuthenticationToken(authReq));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return WResponseEntity.success(tokenProvider.createAccessToken(authentication, authReq.isRememberMe()));
    }

    /**
     * Refresh Access Token.
     *
     * @param refreshReq refresh request, it have refresh token value.
     * @return return new access token
     */
    @PostMapping(URLConstants.M_REFRESH_TOKEN)
    public WResponseEntity<AccessToken> refresh(@RequestBody RefreshReq refreshReq) {
        AccessToken accessToken = tokenProvider.refreshAccessToken(refreshReq.getToken());
        return WResponseEntity.success(accessToken);
    }
}
