package org.example.authorize.app.principal.attempt;

import lombok.RequiredArgsConstructor;
import org.example.authorize.entity.Principal;
import org.example.authorize.entity.PrincipalAttempt;
import org.example.authorize.exception.SaveEntityException;
import org.example.authorize.utils.generator.id.Generator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Principal Attempt Service.
 */
@Service
@RequiredArgsConstructor
public class PrincipalAttemptService {

    private final Generator<String> generator;
    private final PrincipalAttemptRepository principalAttemptRepository;

    /**
     * Create Principal Attempt instance by principal.
     *
     * @param principal the principal object.
     * @return return PrincipalAttemp instance
     */
    public PrincipalAttempt createPrincipalAttempt(Principal principal) {
        if (null != principal && !StringUtils.isEmpty(principal.getId())) {
            PrincipalAttempt attempt = new PrincipalAttempt();
            attempt.setPrincipal(principal);
            attempt.setAttemptCount((short) 0);
            return attempt;
        }
        return null;
    }

    /**
     * Increase attempt count and save it to database.
     *
     * @param principalAttempt PrincipalAttempt instance need to increase count
     * @return return PrincipalAttempt after increase
     */
    @Transactional
    public PrincipalAttempt increaseAttempt(PrincipalAttempt principalAttempt) {
        if (null != principalAttempt) {
            short attemptCount = principalAttempt.getAttemptCount();
            principalAttempt.setAttemptCount(++attemptCount);
            principalAttempt = principalAttemptRepository.save(principalAttempt);
        }
        return principalAttempt;
    }

    /**
     * Reset attempt count.
     *
     * @param principalAttempt PrincipalAttempt instance need to reset count
     * @return return PrincipalAttempt after reset
     */
    @Transactional
    public PrincipalAttempt resetAttempt(PrincipalAttempt principalAttempt) {
        if (null != principalAttempt && principalAttempt.getAttemptCount() > 0) {
            principalAttempt.setAttemptCount((short) 0);
            principalAttempt = principalAttemptRepository.save(principalAttempt);
        }
        return principalAttempt;
    }

    /**
     * Save Principal Attempt instance to database.
     *
     * @param attempt attempt object
     * @return retrun true if save/update successfully
     */
    @Transactional
    public PrincipalAttempt save(PrincipalAttempt attempt) {
        if (null != attempt) {
            return principalAttemptRepository.save(attempt);
        }
        throw new SaveEntityException("PrincipalAttempt is empty, cannot save it");
    }
}
