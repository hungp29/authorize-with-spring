package org.example.authorize.component.httpdefault;

import org.example.authorize.entity.common.Audit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * Default Repository.
 *
 * @param <T> Generic of entity
 */
@NoRepositoryBean
public interface DefaultHttpRepository<T extends Audit<?>> extends JpaRepository<T, Serializable> {
}
