package org.example.authorize.app.account;

import org.example.authorize.app.account.req.AccountReq;
import org.example.authorize.app.account.req.EmailReq;
import org.example.authorize.app.account.req.PhoneReq;
import org.example.authorize.entity.Account;
import org.example.authorize.entity.Role;
import org.example.authorize.security.authentoken.OTPAuthenticationToken;
import org.example.authorize.security.userdetails.OTPUserDetailsService;
import org.example.authorize.security.userdetails.TokenUserDetailsService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.List;
import java.util.Optional;

/**
 * Account Service.
 */
public interface AccountService extends UserDetailsService,
        TokenUserDetailsService<PreAuthenticatedAuthenticationToken>,
        OTPUserDetailsService<OTPAuthenticationToken> {

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
     * @param accountReq information to create new account
     * @return return the account is created
     */
    Account createAndSaveByUsernameAndPassword(AccountReq accountReq);

    /**
     * Find Account by auth data 1.
     *
     * @param authData1 auth data 1 (username, email, phone number)
     * @return return account instance if it exist
     */
    Account findAccountByAuthData1(String authData1);

    /**
     * Update phone number for account.
     *
     * @param id       id of account
     * @param phoneReq phone request object
     * @return return true if update phone number successfully, otherwise return false
     */
    boolean addOrUpdatePhoneNumber(String id, PhoneReq phoneReq);

    /**
     * Update email for account.
     *
     * @param id       id of account
     * @param emailReq email request object
     * @return return true if update email successfully, otherwise return false
     */
    boolean addOrUpdateEmail(String id, EmailReq emailReq);
}
