package org.example.authorize.app.principal;

import lombok.RequiredArgsConstructor;
import org.example.authorize.entity.Principal;
import org.example.authorize.exception.SaveEntityException;
import org.example.authorize.utils.generator.id.Generator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Principal Service.
 */
@Service
@RequiredArgsConstructor
public class PrincipalService {

    private final Generator<String> generator;
    private final PrincipalRepository principalRepository;

    /**
     * Create principal but not save it.
     *
     * @param disabled disable principal when creating or not
     * @return return principal
     */
    public Principal createPrincipal(boolean disabled) {
        Principal principal = new Principal();
        principal.setId(generator.generate());
        principal.setDisabled(disabled);
        return principal;
    }

    /**
     * Create principal (disabled) but not save it.
     *
     * @return return principal
     */
    public Principal createPrincipal() {
        return createPrincipal(true);
    }

    /**
     * Active principal.
     *
     * @param principal the principal will be active
     * @return principal after active
     */
    @Transactional
    public Principal active(Principal principal) {
        if (null != principal) {
            principal.setDisabled(false);
            principal = principalRepository.save(principal);
        }
        return principal;
    }

    /**
     * Save or update principal.
     *
     * @param principal principal need to save or update
     * @return return principal is saved or updated
     */
    public Principal save(Principal principal) {
        if (null != principal) {
            return principalRepository.save(principal);
        }
        throw new SaveEntityException("Principal is empty, cannot save it");
    }
}
