package com.erp.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "payment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "paymentid")
    private Long paymentId;

    @Column(name = "branchcode", nullable = false, length = 50)
    private String branchCode;

    @Column(name = "noofbranch")
    private Integer noOfBranch;

    @Column(name = "branchplan", length = 100)
    private String branchPlan;

    @Column(name = "nooftechnicians")
    private Integer noOfTechnicians;

    @Column(name = "technicianplan", length = 100)
    private String technicianPlan;

    @Column(name = "paymentstatus", length = 20)
    private String paymentStatus;

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
//    @JoinColumn(name = "branchcode", referencedColumnName = "branchcode", insertable = false, updatable = false)
//    private BranchEntity branch;
}