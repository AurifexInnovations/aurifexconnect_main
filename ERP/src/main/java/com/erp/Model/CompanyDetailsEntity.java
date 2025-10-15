package com.erp.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "companydetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "companyid")
    private Long companyId;

    @Column(name = "companyname", nullable = false, length = 100)
    private String companyName;

    @Column(name = "companycode", nullable = false, unique = true, length = 50)
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
}