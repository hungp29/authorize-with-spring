package org.example.authorize.app.account;

import org.example.authorize.entity.Account;
import org.example.authorize.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Account Repository.
 */
public interface AccountRepository extends JpaRepository<Account, String> {

    /**
     * Find account by id.
     *
     * @param id the id of account
     * @return return the account if it's exist
     */
    Optional<Account> findById(String id);

    /**
     * Find accounts by roles.
     *
     * @param roles roles of account
     * @return return list account satisfy condition
     */
    List<Account> findByPrincipal_RolesIn(List<Role> roles);

    /**
     * Find account by username.
     *
     * @param username username of account
     * @return return account if it's exist
     */
    Optional<Account> findByUsername(String username);
}
