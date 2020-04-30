package org.example.authorize.app.account;

import lombok.RequiredArgsConstructor;
import org.example.authorize.app.account.req.AccountReq;
import org.example.authorize.app.account.req.EmailReq;
import org.example.authorize.app.account.req.PhoneReq;
import org.example.authorize.app.authmethod.AuthMethodService;
import org.example.authorize.app.principal.PrincipalService;
import org.example.authorize.entity.Account;
import org.example.authorize.entity.AuthMethod;
import org.example.authorize.entity.Principal;
import org.example.authorize.entity.Role;
import org.example.authorize.enums.AuthType;
import org.example.authorize.exception.AccountInvalidException;
import org.example.authorize.exception.SaveEntityException;
import org.example.authorize.security.UserPrincipal;
import org.example.authorize.security.authentoken.JWTAuthenticationToken;
import org.example.authorize.security.authentoken.OTPAuthenticationToken;
import org.example.authorize.utils.SecurityUtils;
import org.example.authorize.utils.generator.id.Generator;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Account Service.
 */
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final Generator<String> generator;

    private final AuthMethodService authMethodService;
    private final PrincipalService principalService;

    private final AccountRepository accountRepository;


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
     * Create new account by account information.
     *
     * @param accountReq account information
     * @return return the account is created
     */
    public Account createAccount(AccountReq accountReq) {
        // Create new principal
        Principal principal = principalService.createPrincipal();

        // Create new auth method
        AuthMethod authMethod = authMethodService.createAuthMethodUsername(accountReq.getUsername(), accountReq.getPassword());
        principal.setAuthMethods(Collections.singletonList(authMethod));

        // Create new account
        Account account = new Account();
        account.setId(generator.generate());
        account.setFirstName(accountReq.getUsername());
        account.setLastName(accountReq.getLastName());
        account.setPrincipal(principal);
        return account;
    }

    /**
     * Create new account by username and password.
     *
     * @param accountReq information to create new account
     * @return return the account is created
     */
    @Transactional
    public Account createAndSaveByUsernameAndPassword(AccountReq accountReq) {
        // Create new principal
        Principal principal = principalService.createPrincipal();
        principal = principalService.save(principal);

        // Create new auth method
        AuthMethod authMethod = authMethodService.createAuthMethodUsername(accountReq.getUsername(), accountReq.getPassword());
        authMethod.setPrincipal(principal);
        authMethodService.save(authMethod);

        // Create new account
        Account account = new Account();
        account.setId(generator.generate());
        account.setFirstName(accountReq.getUsername());
        account.setPrincipal(principal);
        account = save(account);
        return account;
    }

    /**
     * Save account.
     *
     * @param account the account instance
     * @return return account is saved successfully
     */
    @Transactional
    public Account save(Account account) {
        if (null != account) {
//            if (SecurityUtils.hasPrincipal(account)) {
//                principalService.save(account.getPrincipal());
//            }
//            if (SecurityUtils.hasAuthMethod(account)) {
//                authMethodService.save()
//            }
            return accountRepository.save(account);
        }
        throw new SaveEntityException("Account is empty, cannot save it");
    }

    /**
     * Find Account by auth data 1.
     *
     * @param authData1 auth data 1 (username, email, phone number)
     * @return return account instance if it exist
     */
    public Account findAccountByAuthData1(String authData1) {
        AuthMethod authMethod = authMethodService.findByDetermineId(authData1);
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
        AuthMethod authMethod = authMethodService
                .findByDetermineIdAndAuthTypes(username, AuthType.USERNAME_PASSWORD, AuthType.EMAIL_PASSWORD);

        if (null != authMethod && null != authMethod.getPrincipal()) {
            return UserPrincipal.create(authMethod.getPrincipal().getAccount(), authMethod.getAuthType());
        } else {
            throw new UsernameNotFoundException("Cannot find user by username");
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
            return UserPrincipal.create(accountRepository.findById(principal.getId())
                    .orElse(null), AuthType.REFRESH_TOKEN);
        } else {
            throw new UsernameNotFoundException("Cannot find user by refresh token");
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
        AuthMethod authMethod = authMethodService
                .findByAuthTypeAndDetermineId(AuthType.PHONE_NUMBER, (String) otpToken.getPrincipal());

        if (null != authMethod && null != authMethod.getPrincipal()) {
            return UserPrincipal.create(authMethod.getPrincipal().getAccount(), AuthType.PHONE_NUMBER);
        } else {
            throw new UsernameNotFoundException("Cannot find user by using OTP");
        }
    }

    /**
     * Update phone number for account.
     *
     * @param id       id of account
     * @param phoneReq phone request object
     * @return return true if update successfully, otherwise return false
     */
    @Transactional
    public boolean addOrUpdatePhoneNumber(String id, PhoneReq phoneReq) {
        Account account = accountRepository.findById(id).orElse(null);
        if (SecurityUtils.hasPrincipal(account)) {
            AuthMethod phoneAuthMethod = null;
            // Set or Update phone auth method
            if (SecurityUtils.hasAuthMethod(account)) {
                phoneAuthMethod = account.getPrincipal().getAuthMethods().stream()
                        .filter(authMethod -> AuthType.PHONE_NUMBER.equals(authMethod.getAuthType()))
                        .findFirst()
                        .orElse(null);
            }
            if (null == phoneAuthMethod) {
                phoneAuthMethod = authMethodService.createAuthMethodPhoneNumber(phoneReq.getPhone());
                phoneAuthMethod.setPrincipal(account.getPrincipal());
            } else {
                phoneAuthMethod.setDetermineId(phoneReq.getPhone());
            }
            authMethodService.save(phoneAuthMethod);

            // Set or Update phone number for account
            account.setPhoneNumber(phoneReq.getPhone());
            save(account);
            return true;
        }
        return false;
    }


    /**
     * Update email for account.
     *
     * @param id       id of account
     * @param emailReq email request object
     * @return return true if update successfully, otherwise return false
     */
    @Transactional
    public boolean addOrUpdateEmail(String id, EmailReq emailReq) {
        Account account = accountRepository.findById(id).orElse(null);
        if (SecurityUtils.hasPrincipal(account)) {
            List<AuthMethod> authMethods = account.getPrincipal().getAuthMethods();
            // Get username auth method
            AuthMethod usernameAuthMethod = authMethodService.findAuthMethod(authMethods, AuthType.USERNAME_PASSWORD)
                    .orElseThrow(() -> new AccountInvalidException("Cannot find USERNAME_PASSWORD auth method"));
            // Set/Update email auth method
            AuthMethod emailAuthMethod = authMethodService.findAuthMethod(authMethods, AuthType.EMAIL_PASSWORD)
                    .orElse(null);
            if (null == emailAuthMethod) {
                emailAuthMethod = authMethodService
                        .createAuthMethodEmail(emailReq.getEmail(), usernameAuthMethod.getAuthMethodData());
                emailAuthMethod.setPrincipal(account.getPrincipal());
            } else {
                emailAuthMethod.setDetermineId(emailReq.getEmail());
            }
            authMethodService.save(emailAuthMethod);

            // Set/Update phone number for account
            account.setEmail(emailReq.getEmail());
            save(account);
            return true;
        }
        return false;
    }
}
