package org.example.authorize.config;

import lombok.RequiredArgsConstructor;
import org.example.authorize.app.account.AccountService;
import org.example.authorize.security.SecurityConfigurer;
import org.example.authorize.security.SecurityProblemHandler;
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

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    public static final String[] WHITE_LIST = {
    };

    public static final String[] GET_WHITE_LIST = {
    };

    public static final String[] POST_WHITE_LIST = {
            URLConstants.C_AUTHENTICATION + URLConstants.M_AUTHENTICATION
    };

    public static final String[] PUT_WHITE_LIST = {
    };

    public static final String[] DELETE_WHITE_LIST = {
    };

    private final PasswordEncode passwordEncode;
    private final AccountService accountService;
    private final SecurityProblemHandler securityProblemHandler;
    private final SecurityConfigurer securityConfigurer;

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
        auth.
                authenticationProvider(usernamePasswordAuthenticationProvider());
//                authenticationProvider(preAuthenticatedAuthenticationProvider());

    }

    /**
     * Authentication Provider for Username Password.
     *
     * @return Authentication Provider
     */
    @Bean
    public DaoAuthenticationProvider usernamePasswordAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncode.getPasswordEncoder());
        provider.setUserDetailsService(accountService);
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
