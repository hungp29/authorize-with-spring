package org.example.authorize.events;

import lombok.RequiredArgsConstructor;
import org.example.authorize.app.principal.PrincipalService;
import org.example.authorize.app.principal.attempt.PrincipalAttemptService;
import org.example.authorize.config.prop.ApplicationProperties;
import org.example.authorize.entity.Principal;
import org.example.authorize.entity.PrincipalAttempt;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationEventListener {

    private final PrincipalService principalService;
    private final PrincipalAttemptService principalAttemptService;
    private final ApplicationProperties appProps;

    /**
     * When login failure then increase count.
     *
     * @param event Authentication Failure event
     */
    @EventListener(AuthenticationFailureBadCredentialsEvent.class)
    public void onLoginFailure(AuthenticationFailureBadCredentialsEvent event) {
        if (event.getException().getClass().equals(UsernameNotFoundException.class)) {
            return;
        }

        // Username can be username, email or phone number
        String authData1 = event.getAuthentication().getName();
        Principal principal = principalService.findPrincipalByAuthData1(authData1);

        if (null != principal) {
            PrincipalAttempt attempt = principal.getPrincipalAttempt();
            if (null == attempt) {
                attempt = principalAttemptService.createPrincipalAttempt(principal);
            }
            principalAttemptService.increaseAttempt(attempt);

            if (attempt.getAttemptCount() >= appProps.getLoginAttemptsThreshold()) {
                principalService.lockPrincipal(principal);
            }
        }
    }

    /**
     * When login successfully then reset attempt count to zero.
     *
     * @param event Authentication Success event
     */
    @EventListener(AuthenticationSuccessEvent.class)
    public void onLoginSuccess(AuthenticationSuccessEvent event) {
        // Username can be username, email or phone number
        String authData1 = event.getAuthentication().getName();
        Principal principal = principalService.findPrincipalByAuthData1(authData1);

        if (null != principal && null != principal.getPrincipalAttempt()) {
            principalAttemptService.resetAttempt(principal.getPrincipalAttempt());
        }
    }
}
