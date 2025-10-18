package com.erp.Config;

import com.erp.Model.Admin;
import com.erp.Model.User;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.domain.AbstractAuditable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Auditable extends AbstractAuditable<Admin, Long> {

    @ManyToOne
    @JoinColumn(name = "created_by", updatable = false)
    private Admin createdBy;

    @ManyToOne
    @JoinColumn(name = "last_modified_by")
    private Admin lastModifiedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date", updatable = false)
    private Date createdDate;

    @Column(name = "last_modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    @Nullable
    private Date lastModifiedDate;

}
