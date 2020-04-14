package org.example.authorize.app.account;

import lombok.RequiredArgsConstructor;
import org.example.authorize.app.authmethod.AuthMethodRepository;
import org.example.authorize.app.authmethod.AuthMethodService;
import org.example.authorize.app.principal.PrincipalRepository;
import org.example.authorize.app.principal.PrincipalService;
import org.example.authorize.entity.Account;
import org.example.authorize.entity.AuthMethod;
import org.example.authorize.entity.Principal;
import org.example.authorize.entity.Role;
import org.example.authorize.utils.PasswordEncode;
import org.example.authorize.utils.generator.Generator;
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
public class AccountService {

    private final AccountRepository accountRepository;
    private final PrincipalRepository principalRepository;
    private final AuthMethodRepository authMethodRepository;

    private final AuthMethodService authMethodService;
    private final PrincipalService principalService;

    private final PasswordEncode passwordEncode;
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
}
