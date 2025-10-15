package com.erp.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "branch")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "branchid")
    private Long branchId;

    @Column(name = "branchno", nullable = false, length = 50)
    private String branchNo;

    @Column(name = "branchname", nullable = false, length = 100)
    private String branchName;

    @Column(name = "branchcode", nullable = false, unique = true, length = 50)
    private String branchCode;

    @Column(name = "location", length = 100)
    private String location;

    @Column(name = "companycode", nullable = false, length = 50)
    private String companyCode;

    @Column(name = "activeYn", length = 1)
    private String activeYn;

    @Column(name = "createdby", length = 100)
    private String createdBy;

    @Column(name = "createdon")
    private Timestamp createdOn;

    @Column(name = "deletedby", length = 100)
    private String deletedBy;

    @Column(name = "deletedon")
    private Timestamp deletedOn;

//    @ManyToOne
//    @JoinColumn(name = "companycode", referencedColumnName = "companycode", insertable = false, updatable = false)
//    private CompanyDetailsEntity company;
}