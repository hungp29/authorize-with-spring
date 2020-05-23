package org.example.authorize.component.httpdefault;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.authorize.entity.common.Audit;
import org.example.authorize.response.WResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Default Rest Controller.
 *
 * @param <T> Generic of entity
 */
@Slf4j
public class DefaultHttpRestController<T extends Audit<?>> {

    private DefaultHttpService<T> service;

    /**
     * Set Service.
     *
     * @param service service
     */
    @Autowired
    public void setService(DefaultHttpService<T> service) {
        this.service = service;
    }

    /**
     * API get.
     *
     * @param id id of entity.
     * @return DTO of entity
     */
    @GetMapping("/{id}")
    public WResponseEntity<String> get(@PathVariable String id) {
        return WResponseEntity.success(id + " ok");
    }
}
