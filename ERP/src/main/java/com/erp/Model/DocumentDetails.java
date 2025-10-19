package com.erp.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "document_details")
@Getter
@Setter
@ToString(exclude = "documentDetails")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DocumentDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "document_name")
    private String documentName;

    @Column(name = "document_url", columnDefinition = "TEXT")
    private String documentUrl;

    @ManyToOne
    @JoinColumn(name = "company_details_id")
    @JsonBackReference
    private CompanyDetails companyDetails;



}
