package org.example.authorize.entity.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
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
public abstract class Audit<T> implements Serializable {

    @Column
    @CreatedBy
    private T createBy;

    @Column
    @CreatedDate
    private LocalDateTime createAt;

    @Column
    @LastModifiedBy
    private T updateBy;

    @Column
    @LastModifiedDate
    private LocalDateTime updateAt;
}
