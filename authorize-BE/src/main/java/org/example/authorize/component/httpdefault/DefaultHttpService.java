package org.example.authorize.component.httpdefault;

import lombok.extern.slf4j.Slf4j;
import org.example.authorize.entity.common.Audit;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Default Service.
 *
 * @param <T> Generic of entity
 */
@Slf4j
public class DefaultHttpService<T extends Audit<?>> {

    private DefaultHttpRepository<T> repository;

    /**
     * Set repository.
     *
     * @param repository repository
     */
    @Autowired
    public void setRepository(DefaultHttpRepository<T> repository) {
        this.repository = repository;
    }
}
