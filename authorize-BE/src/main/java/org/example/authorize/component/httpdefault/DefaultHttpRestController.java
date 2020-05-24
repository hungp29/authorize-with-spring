package org.example.authorize.component.httpdefault;

import lombok.extern.slf4j.Slf4j;
import org.example.authorize.entity.common.Audit;
import org.example.authorize.response.WResponseEntity;
import org.example.authorize.utils.constants.URLConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

/**
 * Default Rest Controller.
 *
 * @param <T> Generic of entity
 */
@Slf4j
public class DefaultHttpRestController<T extends Audit<?>> {

    private DefaultHttpService<T> service;

    /**
     * Create new entity.
     *
     * @param createRequestDTO the data of entity
     * @return DTO of entity
     */
    @PostMapping(URLConstants.COMMON_CREATE)
    public WResponseEntity<Object> create(@RequestBody Object createRequestDTO) {
        return WResponseEntity.success(service.createAndSaveEntity(createRequestDTO));
    }

    /**
     * API get.
     *
     * @param id   id of entity.
     * @param <ID> generic of Id
     * @return DTO of entity
     */
    @GetMapping(URLConstants.COMMON_GET)
    public <ID extends Serializable> WResponseEntity<Object> get(@PathVariable ID id) {
        return WResponseEntity.success(service.getEntity(id));
    }

    /**
     * API update.
     *
     * @param id               id of entity
     * @param updateRequestDTO update request DTO
     * @param <ID>             generic of Id
     * @return update response DTO
     */
    @PutMapping(URLConstants.COMMON_UPDATE)
    public <ID extends Serializable> WResponseEntity<Object> update(@PathVariable ID id,
                                                                    @RequestBody Object updateRequestDTO) {
        return WResponseEntity.success(service.updateEntity(id, updateRequestDTO));
    }

    /**
     * API delete.
     *
     * @param id   id of entity
     * @param <ID> generic of Id
     * @return true if entity is deleted successfully
     */
    @DeleteMapping(URLConstants.COMMON_DELETE)
    public <ID extends Serializable> WResponseEntity<Boolean> delete(@PathVariable ID id) {
        return WResponseEntity.success(service.deleteEntity(id));
    }

    /**
     * Set Service.
     *
     * @param service service
     */
    @Autowired
    public void setService(DefaultHttpService<T> service) {
        this.service = service;
    }
}
