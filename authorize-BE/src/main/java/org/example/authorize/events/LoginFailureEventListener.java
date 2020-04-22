package org.example.authorize.events;

import lombok.RequiredArgsConstructor;
import org.example.authorize.app.principal.PrincipalService;
import org.example.authorize.entity.Principal;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginFailureEventListener {

    private final PrincipalService principalService;

    @EventListener(AuthenticationFailureBadCredentialsEvent.class)
    public void onLoginFailure(AuthenticationFailureBadCredentialsEvent event) {
        if (event.getException().getClass().equals(UsernameNotFoundException.class)) {
            return;
        }

        // Username can be username, email or phone number
        String authData1 = event.getAuthentication().getName();
        Principal principal = principalService.findPrincipalByAuthData1(authData1);

        if (null != principal) {
            short attemptCount = principal.getAttemptCount();
            principal.setAttemptCount(++attemptCount);
            principalService.save(principal);
        }
        System.out.println("AAAA");
    }
}
