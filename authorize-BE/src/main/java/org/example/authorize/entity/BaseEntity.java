package org.example.authorize.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Base Entity.
 */
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class BaseEntity implements Serializable {

    @Id
    @Size(max = 32)
    private String id;

    @Size(max = 32)
    @Column
    @CreatedBy
    private String createBy;

    @Column
    @CreatedDate
    private LocalDateTime createAt;

    @Size(max = 32)
    @Column
    @LastModifiedBy
    private String updateBy;

    @Column
    @LastModifiedDate
    private LocalDateTime updateAt;
}
