package org.example.authorize.config;

import lombok.RequiredArgsConstructor;
import org.example.authorize.app.account.AccountService;
import org.example.authorize.security.SecurityConfigurer;
import org.example.authorize.security.SecurityProblemHandler;
import org.example.authorize.security.authenprovider.OTPAuthenticationProvider;
import org.example.authorize.utils.OTPSupport;
import org.example.authorize.utils.PasswordEncode;
import org.example.authorize.utils.constants.URLConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

/**
 * Security Configuration.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    /**
     * White list for all http method.
     */
    public static final String[] WHITE_LIST = {
    };

    /**
     * White list for GET method.
     */
    public static final String[] GET_WHITE_LIST = {
    };

    /**
     * White list for POST method.
     */
    public static final String[] POST_WHITE_LIST = {
            URLConstants.C_AUTHENTICATION + URLConstants.M_AUTHENTICATION,
            URLConstants.C_AUTHENTICATION + URLConstants.M_REFRESH_TOKEN,
            URLConstants.C_AUTHENTICATION + URLConstants.M_OTP,
            URLConstants.C_AUTHENTICATION + URLConstants.M_OTP_VERIFY
    };

    /**
     * White list for PUT method.
     */
    public static final String[] PUT_WHITE_LIST = {
    };

    /**
     * White list for DELETE method.
     */
    public static final String[] DELETE_WHITE_LIST = {
    };

    private final PasswordEncode passwordEncode;
    private final OTPSupport otpSupport;
    private final AccountService accountService;
    private final SecurityProblemHandler securityProblemHandler;
    private final SecurityConfigurer securityConfigurer;

    /**
     * Create AuthenticationManager bean.
     *
     * @return return AuthenticationManager bean
     * @throws Exception
     */
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * Configuration list authentication provider.
     *
     * @param auth Authentication manager builder
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        auth
                .authenticationProvider(usernamePasswordAuthenticationProvider())
                .authenticationProvider(preAuthenticatedAuthenticationProvider())
                .authenticationProvider(otpAuthenticationProvider());

    }

    /**
     * Authentication Provider for Username Password.
     *
     * @return DAO Authentication Provider
     */
    @Bean
    public DaoAuthenticationProvider usernamePasswordAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncode.getPasswordEncoder());
        provider.setUserDetailsService(accountService);
        return provider;
    }

    /**
     * Authentication Provider for Refresh Token.
     *
     * @return Pre Authenticated Authentication Provider
     */
    @Bean
    public PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider() {
        PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
        provider.setPreAuthenticatedUserDetailsService(accountService);
        return provider;
    }

    /**
     * Authentication Provider for OTP Token.
     *
     * @return OTP Authentication Provider
     */
    @Bean
    public OTPAuthenticationProvider otpAuthenticationProvider() {
        OTPAuthenticationProvider provider = new OTPAuthenticationProvider();
        provider.setOtpUserDetailsService(accountService);
        provider.setOtpSupport(otpSupport);
        return provider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                csrf().disable().
                exceptionHandling().
                authenticationEntryPoint(securityProblemHandler).
                accessDeniedHandler(securityProblemHandler).
                and().
                sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
                and().
                authorizeRequests().
                antMatchers(WHITE_LIST).permitAll().
                antMatchers(HttpMethod.POST, POST_WHITE_LIST).permitAll().
                antMatchers(HttpMethod.GET, GET_WHITE_LIST).permitAll().
                antMatchers(HttpMethod.PUT, PUT_WHITE_LIST).permitAll().
                antMatchers(HttpMethod.DELETE, DELETE_WHITE_LIST).permitAll().
                anyRequest().authenticated().
                and().
                httpBasic().
                and().
                apply(securityConfigurer);

    }
}
