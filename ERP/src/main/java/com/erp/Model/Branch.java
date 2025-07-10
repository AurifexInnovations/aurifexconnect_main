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
    private long branchId;

    private String branchName;

    private String location;

    private String contactInfo;

    @CreatedDate
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private BranchStatus branchStatus;

    @OneToMany(mappedBy = "branch")
    private List<Inventory> inventories;

    @OneToMany(mappedBy = "branch")
    private List<Staff> staffList;

}
