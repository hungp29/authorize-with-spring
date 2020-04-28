package org.example.authorize.app.account;

import lombok.RequiredArgsConstructor;
import org.example.authorize.app.account.requestobject.PhoneReq;
import org.example.authorize.app.authmethod.AuthMethodRepository;
import org.example.authorize.app.authmethod.AuthMethodService;
import org.example.authorize.app.principal.PrincipalRepository;
import org.example.authorize.app.principal.PrincipalService;
import org.example.authorize.entity.Account;
import org.example.authorize.entity.AuthMethod;
import org.example.authorize.entity.Principal;
import org.example.authorize.entity.Role;
import org.example.authorize.enums.AuthType;
import org.example.authorize.security.UserPrincipal;
import org.example.authorize.security.authentoken.JWTAuthenticationToken;
import org.example.authorize.security.authentoken.OTPAuthenticationToken;
import org.example.authorize.utils.generator.id.Generator;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Account Service.
 */
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final PrincipalRepository principalRepository;
    private final AuthMethodRepository authMethodRepository;

    private final AuthMethodService authMethodService;
    private final PrincipalService principalService;

    private final Generator<String> generator;

    /**
     * Find account by id.
     *
     * @param id the id of account
     * @return return the account if it's exist
     */
    public Optional<Account> findAccountById(String id) {
        return accountRepository.findById(id);
    }

    /**
     * Find accounts have role specific.
     *
     * @param role the role of account
     * @return list account
     */
    public List<Account> findAccountByRole(Role role) {
        return accountRepository.findByPrincipal_RolesIn(Collections.singletonList(role));
    }

    /**
     * Create new account by username and password.
     *
     * @param username username of account
     * @param password password of account
     * @return return the account is created
     */
    @Transactional
    public Account createAndSaveByUsernameAndPassword(String username, String password) {
        // Create new principal
        Principal principal = principalService.createPrincipal();
        principal = principalRepository.save(principal);

        // Create new auth method
        AuthMethod authMethod = authMethodService.createAuthMethodByUsernameAndPassword(username, password);
        authMethod.setPrincipal(principal);
        authMethodRepository.save(authMethod);

        // Create new account
        Account account = new Account();
        account.setId(generator.generate());
        account.setFirstName(username);
        account.setPrincipal(principal);
        account = accountRepository.save(account);

        return account;
    }

    /**
     * Find Account by auth data 1.
     *
     * @param authData1 auth data 1 (username, email, phone number)
     * @return return account instance if it exist
     */
    public Account findAccountByAuthData1(String authData1) {
        AuthMethod authMethod = authMethodService.findByAuthData1(authData1);
        if (null != authMethod && null != authMethod.getPrincipal()) {
            return authMethod.getPrincipal().getAccount();
        }
        return null;
    }

    /**
     * Load UserDetails by username. It use for spring security to checking authenticate.
     *
     * @param username username of account
     * @return return UserDetails instance
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        AuthMethod authMethod = authMethodService.findByAuthTypeAndAuthData1(AuthType.USERNAME_PASSWORD, username);

        if (null != authMethod && null != authMethod.getPrincipal()) {
            return UserPrincipal.create(authMethod.getPrincipal().getAccount(), AuthType.USERNAME_PASSWORD);
        } else {
            throw new UsernameNotFoundException("Cannot find user");
        }
    }

    /**
     * Load UserDetails by token.
     *
     * @param token JWT authentication provider
     * @return return UserDetails instance
     */
    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) {
        JWTAuthenticationToken jwtAuthenticationToken = (JWTAuthenticationToken) token;
        UserPrincipal principal = (UserPrincipal) jwtAuthenticationToken.getAuthentication().getPrincipal();

        if (null != principal) {
            String accountId = principal.getId();
            return UserPrincipal.create(accountRepository.findById(accountId).orElse(null), AuthType.REFRESH_TOKEN);
        } else {
            throw new UsernameNotFoundException("Cannot find user");
        }
    }

    /**
     * Load UserDetails by otp token.
     *
     * @param otpToken The otp authentication token
     * @return return UserDetails instance
     */
    @Override
    public UserDetails loadUserDetailsForOTP(OTPAuthenticationToken otpToken) {
        AuthMethod authMethod = authMethodService.findByAuthTypeAndAuthData1(AuthType.PHONE_NUMBER, (String) otpToken.getPrincipal());

        if (null != authMethod && null != authMethod.getPrincipal()) {
            return UserPrincipal.create(authMethod.getPrincipal().getAccount(), AuthType.PHONE_NUMBER);
        } else {
            throw new UsernameNotFoundException("Cannot find user");
        }
    }

    /**
     * Update phone number for account.
     *
     * @param id       id of account
     * @param phoneReq phone request object
     * @return return true if update successfully, otherwise return false
     */
    public boolean addOrUpdatePhoneNumber(String id, PhoneReq phoneReq) {
        Account account = accountRepository.findById(id).orElse(null);
        if (null != account && null != account.getPrincipal() && !CollectionUtils.isEmpty(account.getPrincipal().getAuthMethods())) {
            AuthMethod phoneAuthMethod = account.getPrincipal().getAuthMethods().stream()
                    .filter(authMethod -> AuthType.PHONE_NUMBER.equals(authMethod.getAuthType()))
                    .findFirst()
                    .orElse(null);
            if (null == phoneAuthMethod) {
                phoneAuthMethod = authMethodService.createAuthMethodByPhoneNumber(phoneReq.getPhone());
                phoneAuthMethod.setPrincipal(account.getPrincipal());
            }
            phoneAuthMethod.setAuthData1(phoneReq.getPhone());
            authMethodService.save(phoneAuthMethod);
            return true;
        }
        return false;
    }
}
