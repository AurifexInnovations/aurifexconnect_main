package com.erp.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "technician")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TechnicianEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "technicianid")
    private Long technicianId;

    @Column(name = "technicianname", nullable = false, length = 100)
    private String technicianName;

    @Column(name = "contact", length = 15)
    private String contact;

    @Column(name = "age")
    private Integer age;

    @Column(name = "gender", length = 10)
    private String gender;

    @Column(name = "branchcode", nullable = false, length = 50)
    private String branchCode;

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