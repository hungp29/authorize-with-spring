package org.example.authorize.component.httpdefault;

import lombok.extern.slf4j.Slf4j;
import org.example.authorize.entity.common.Audit;
import org.example.authorize.exception.SaveEntityException;
import org.example.authorize.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * Default Service.
 *
 * @param <T> Generic of entity
 */
@Slf4j
public class DefaultHttpService<T extends Audit<?>> {

    private DefaultHttpRepository<T> repository;

    protected Object createAndSaveEntity(Object entityDTO) {
        ObjectUtils.getGenericClass(this.getClass());
        return null;
    }

    /**
     * Save entity to database.
     *
     * @param entity entity data
     * @return the entity is created successfully
     */
    @Transactional
    protected T save(T entity) {
        if (null != entity) {
            return repository.save(entity);
        }
        throw new SaveEntityException(ObjectUtils.getGenericClass(this.getClass()) + " is empty, cannot save it");
    }

    /**
     * Delete entity.
     *
     * @param entity entity need to delete
     */
    @Transactional
    protected void delete(T entity) {
        if (null != entity) {
            repository.delete(entity);
        }
    }

    /**
     * Delete entity by id.
     *
     * @param id   id of entity
     * @param <ID> generic of id, it must be extends from Serializable
     */
    @Transactional
    protected <ID extends Serializable> void deleteById(ID id) {
        if (null != id) {
            repository.deleteById(id);
        }
    }

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
