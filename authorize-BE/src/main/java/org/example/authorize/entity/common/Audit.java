package org.example.authorize.entity.common;

import lombok.Getter;
import lombok.Setter;
import org.example.authorize.app.product.dto.CreateProductRequestDTO;
import org.example.authorize.component.httpdefault.IgnoreMapping;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Audit entity.
 *
 * @param <T> class of id
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Audit<T extends Serializable> implements Serializable {

    @Id
    @GeneratedValue(generator = "id_generator")
    @GenericGenerator(
            name = "id_generator",
            strategy = "org.example.authorize.component.generator.id.StringIdentifierGenerator"
    )
    private T id;

    @Column
    @CreatedBy
    @IgnoreMapping(CreateProductRequestDTO.class)
    private T createBy;

    @Column
    @CreatedDate
    @IgnoreMapping(CreateProductRequestDTO.class)
    private LocalDateTime createAt;

    @Column
    @LastModifiedBy
    @IgnoreMapping(CreateProductRequestDTO.class)
    private T updateBy;

    @Column
    @LastModifiedDate
    @IgnoreMapping(CreateProductRequestDTO.class)
    private LocalDateTime updateAt;
}
