package org.example.authorize.app.account;

import org.example.authorize.entity.Account;
import org.example.authorize.entity.Role;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.List;
import java.util.Optional;

/**
 * Account Service.
 */
public interface AccountService extends UserDetailsService, AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    /**
     * Find account by id.
     *
     * @param id the id of account
     * @return return the account if it's exist
     */
    Optional<Account> findAccountById(String id);

    /**
     * Find accounts have role specific.
     *
     * @param role the role of account
     * @return list account
     */
    List<Account> findAccountByRole(Role role);

    /**
     * Create new account by username and password.
     *
     * @param username username of account
     * @param password password of account
     * @return return the account is created
     */
    Account createAndSaveByUsernameAndPassword(String username, String password);

    /**
     * Find Account by auth data 1.
     *
     * @param authData1 auth data 1 (username, email, phone number)
     * @return return account instance if it exist
     */
    Account findAccountByAuthData1(String authData1);
}
