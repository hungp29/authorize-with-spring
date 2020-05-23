package org.example.authorize.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.authorize.app.product.dto.CreateProductRequestDTO;
import org.example.authorize.component.httpdefault.CreateRequestClassDTO;
import org.example.authorize.entity.common.Audit;
import org.example.authorize.entity.common.IdPrefixValue;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Size;

/**
 * Product entity.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@IdPrefixValue("PRO")
@CreateRequestClassDTO(CreateProductRequestDTO.class)
public class Product extends Audit<String> {

    @Column
    @Size(max = 256)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column(columnDefinition = "text")
    private String image;
}
