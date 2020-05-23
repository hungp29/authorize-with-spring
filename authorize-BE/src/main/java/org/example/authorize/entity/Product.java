package org.example.authorize.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.authorize.app.common.dto.CreateResponseDTO;
import org.example.authorize.app.product.dto.CreateProductRequestDTO;
import org.example.authorize.app.product.dto.ProductResponseDTO;
import org.example.authorize.component.httpdefault.dtoconfig.CreateRequestClassDTO;
import org.example.authorize.component.httpdefault.dtoconfig.CreateResponseClassDTO;
import org.example.authorize.component.httpdefault.dtoconfig.GetResponseClassDTO;
import org.example.authorize.entity.common.Audit;
import org.example.authorize.entity.common.IdPrefixValue;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Size;

/**
 * Product entity.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@IdPrefixValue("PRO")
@CreateRequestClassDTO(CreateProductRequestDTO.class)
@CreateResponseClassDTO(CreateResponseDTO.class)
@GetResponseClassDTO(ProductResponseDTO.class)
public class Product extends Audit<String> {

    @Column
    @Size(max = 256)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column(columnDefinition = "text")
    private String image;
}
