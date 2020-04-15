package org.example.authorize.app.account;

import org.example.authorize.entity.Account;
import org.example.authorize.entity.Role;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

/**
 * Account Service.
 */
public interface AccountService extends UserDetailsService {

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
}
