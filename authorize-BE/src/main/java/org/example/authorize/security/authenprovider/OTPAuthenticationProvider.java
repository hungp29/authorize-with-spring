package org.example.authorize.security.authenprovider;

import lombok.extern.slf4j.Slf4j;
import org.example.authorize.security.authentoken.OTPAuthenticationToken;
import org.example.authorize.security.userdetails.OTPUserDetailsService;
import org.example.authorize.utils.OTPSupport;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.util.Assert;

/**
 * OTP Authentication Provider.
 */
@Slf4j
public class OTPAuthenticationProvider implements AuthenticationProvider, InitializingBean, Ordered {

    private OTPUserDetailsService<OTPAuthenticationToken> otpUserDetailsService = null;
    private UserDetailsChecker userDetailsChecker = new AccountStatusUserDetailsChecker();
    private OTPSupport otpSupport = null;
    private boolean throwExceptionWhenTokenExpire = true;

    private int order = -1;

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(otpUserDetailsService,
                "An OTPUserDetailsService must be set");
        Assert.notNull(otpSupport,
                "An OTPSupport must be set");
    }

    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!supports(authentication.getClass())) {
            return null;
        }

        if (authentication.getPrincipal() == null) {
            log.debug("No pre-authenticated principal found in request.");
            return null;
        }
        if (authentication.getCredentials() == null) {
            log.debug("No pre-authenticated credentials found in request.");
            return null;
        }

        OTPAuthenticationToken otpAuthentication = (OTPAuthenticationToken) authentication;

        if (!otpSupport.verifyHOTP((String) otpAuthentication.getPrincipal(), (String) otpAuthentication.getCredentials())) {
            if (throwExceptionWhenTokenExpire) {
                throw new BadCredentialsException("The code is expiration or invalid");
            }
        }

        UserDetails userDetails = otpUserDetailsService.loadUserDetailsForOTP(otpAuthentication);
        userDetailsChecker.check(userDetails);

        OTPAuthenticationToken result = new OTPAuthenticationToken(
                userDetails, authentication.getCredentials(), userDetails.getAuthorities());
        result.setDetails(authentication.getDetails());

        return result;
    }

    public void setOtpUserDetailsService(OTPUserDetailsService<OTPAuthenticationToken> otpUserDetailsService) {
        Assert.notNull(otpUserDetailsService, "OTPUserDetailsService cannot be null");
        this.otpUserDetailsService = otpUserDetailsService;
    }

    public void setOtpSupport(OTPSupport otpSupport) {
        Assert.notNull(otpSupport, "OTPSupport cannot be null");
        this.otpSupport = otpSupport;
    }

    public void setThrowExceptionWhenTokenExpire(boolean throwExceptionWhenTokenExpire) {
        this.throwExceptionWhenTokenExpire = throwExceptionWhenTokenExpire;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OTPAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
