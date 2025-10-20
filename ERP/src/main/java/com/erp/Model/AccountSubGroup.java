package com.erp.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "account_subgroup")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class AccountSubGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subgroup_id")
    private Long subgroupId;

    @Column(name = "subgroup_name", nullable = false)
    private String subgroupName;

    @Column(name = "subgroup_code", nullable = false, length = 15)
    private String subgroupCode;

    @Column(name = "description")
    private String description;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;

    @LastModifiedBy
    @Column(name = "modified_by")
    private String modifiedBy;

    @LastModifiedDate
    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private AccountGroup accountGroup;

    @OneToMany(mappedBy = "accountSubGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ledger> ledgers;

    // Constructors
    public AccountSubGroup() {}

    public AccountSubGroup(String subgroupName, String subgroupCode, String description, AccountGroup accountGroup) {
        this.subgroupName = subgroupName;
        this.subgroupCode = subgroupCode;
        this.description = description;
        this.accountGroup = accountGroup;
        this.isActive = true;
    }
}
