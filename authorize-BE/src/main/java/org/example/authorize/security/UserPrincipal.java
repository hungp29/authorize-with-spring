package org.example.authorize.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.example.authorize.entity.Account;
import org.example.authorize.entity.AuthMethod;
import org.example.authorize.entity.Principal;
import org.example.authorize.enums.AuthType;
import org.example.authorize.exception.AccountInvalidException;
import org.example.authorize.utils.CommonUtils;
import org.example.authorize.utils.SecurityUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {

    private String id;

    private String principalId;

    private String firstName;

    private String lastName;

    private String username;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    private boolean enabled;

    private LocalDateTime expireDate;

    private short attemptCount;

    public UserPrincipal(String id, String principalId, String firstName, String lastName, String username, String password, Collection<? extends GrantedAuthority> authorities, boolean enabled, LocalDateTime expireDate, short attemptCount) {
        this.id = id;
        this.principalId = principalId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.enabled = enabled;
        this.expireDate = expireDate;
        this.attemptCount = attemptCount;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return null == expireDate || !expireDate.isAfter(LocalDateTime.now());
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public String getId() {
        return id;
    }

    public String getPrincipalId() {
        return principalId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    /**
     * Create UserPrincipal instance from Account entity.
     *
     * @param account  account entity
     * @param authType auth type using to authentication
     * @return UserPrincipal instance
     */
    public static UserPrincipal create(Account account, AuthType authType) {
        if (null == account) {
            throw new UsernameNotFoundException("Can't fine the account");
        }
        if (!SecurityUtils.hasPrincipal(account)) {
            throw new AccountInvalidException("Account don't have principal information");
        }
        if (!SecurityUtils.hasAuthMethod(account)) {
            throw new AccountInvalidException("Account don't have any authentication method");
        }
        if (!SecurityUtils.hasRole(account)) {
            throw new AccountInvalidException("Account don't have any role");
        }

        // Get principal
        Principal principal = account.getPrincipal();

        // Get list granted authority
        List<GrantedAuthority> grantedAuthorities = principal.getRoles()
                .stream().map(BGrantedAuthority::create).collect(Collectors.toList());

        // List auth method
        List<AuthMethod> authMethods = principal.getAuthMethods();

        AuthMethod authMethodUsed = null;
        if (!AuthType.REFRESH_TOKEN.equals(authType)) {
            authMethodUsed = authMethods.stream()
                    .filter(authMethod -> authMethod.getAuthType().equals(authType)).findFirst()
                    .orElseThrow(() -> new AccountInvalidException("Cannot find the " + authType.getCode() + " authentication method"));
        }

        // Get Username and Password
        String username = "";
        String password = "";
        if (AuthType.USERNAME_PASSWORD.equals(authType) ||
                AuthType.EMAIL_PASSWORD.equals(authType)) {
            username = authMethodUsed.getAuthData1();
            password = authMethodUsed.getAuthData2();
        } else if (AuthType.REFRESH_TOKEN.equals(authType)) {
            username = CommonUtils.getFistValueNotEmpty(account.getUsername(), account.getEmail(), account.getId());
        }

        return new UserPrincipal(account.getId(), principal.getId(), account.getFirstName(), account.getLastName(),
                username, password, grantedAuthorities, !principal.isDisabled(), principal.getExpireDate(), principal.getAttemptCount());
    }

}
