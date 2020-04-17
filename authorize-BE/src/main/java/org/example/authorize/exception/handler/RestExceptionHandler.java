package org.example.authorize.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.example.authorize.response.WResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    /**
     * Handler Authentication Exception.
     *
     * @param e authentication exception
     * @return error response with code 401
     */
    @ExceptionHandler({AuthenticationException.class})
    public WResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
        log.error(e.getMessage(), e);
        return WResponseEntity.error(e.getMessage());
    }

    /**
     * Handler Access Denied Exception.
     *
     * @param e access denied exception
     * @return error response with code 403
     */
    @ExceptionHandler({AccessDeniedException.class})
    public WResponseEntity<String> handleAccessDeniedException(AccessDeniedException e) {
        log.error(e.getMessage(), e);
        return WResponseEntity.error(e.getMessage());
    }

    @ExceptionHandler({BadCredentialsException.class,
            InternalAuthenticationServiceException.class})
    public WResponseEntity<String> handleBadCredentialException(Exception e) {
        log.error(e.getMessage(), e);
        return WResponseEntity.error(e.getMessage());
    }

    /**
     * Handle disabled exception.
     *
     * @param e disable exception
     * @return error response
     */
    @ExceptionHandler(DisabledException.class)
    public WResponseEntity<String> handleAccountDisableException(DisabledException e) {
        log.error(e.getMessage(), e);
        return WResponseEntity.error(e.getMessage());
    }

    /**
     * Handler unknown exception.
     *
     * @param e exception
     * @return error response
     */
    @ExceptionHandler(Exception.class)
    public WResponseEntity<String> handleException(Exception e) {
        log.error(e.getMessage(), e);

        return WResponseEntity.error(e.getMessage());
    }
}
