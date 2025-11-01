package com.erp.Model;

import com.erp.Enum.GroupType;
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
@Table(name = "account_group")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class AccountGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "group_name", nullable = false, unique = true)
    private String groupName;

    @Column(name = "group_code", nullable = false, unique = true, length = 10)
    private String groupCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "group_type", nullable = false)
    private GroupType groupType;

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

    @OneToMany(mappedBy = "accountGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AccountSubGroup> accountSubGroups;

    @OneToMany(mappedBy = "accountGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ledger> ledgers;

    // Constructors
    public AccountGroup() {}

    public AccountGroup(String groupName, String groupCode, GroupType groupType, String description) {
        this.groupName = groupName;
        this.groupCode = groupCode;
        this.groupType = groupType;
        this.description = description;
        this.isActive = true;
    }
}
