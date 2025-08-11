package com.erp.Model;

import com.erp.Enum.BranchStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "branch_id")
    private long branchId;

    @Column(name = "branch_name")
    private String branchName;

    @Column(name = "location")
    private String location;

    @Column(name = "contact_info")
    private String contactInfo;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "branch_status")
    private BranchStatus branchStatus;

    @OneToMany(mappedBy = "branch")
    @JsonIgnore
    private List<Inventory> inventories;

    @OneToMany(mappedBy = "branch")
    private List<Staff> staffList;
}
