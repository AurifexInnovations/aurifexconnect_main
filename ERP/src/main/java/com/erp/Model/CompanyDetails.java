package com.erp.Model;

import com.erp.Enum.IndustryType;
import com.erp.Enum.ReviewStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractAuditable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "company_details")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString(exclude = "documentDetails")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CompanyDetails extends AbstractAuditable<Admin, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "company_details_seq_gen")
    @SequenceGenerator(name = "company_details_seq_gen", sequenceName = "company_details_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "industry_type")
    private IndustryType industryType;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "gst_number")
    private String gstNumber;

    @Column(name = "pan_number")
    private String panNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "review_status")
    private ReviewStatus reviewStatus = ReviewStatus.PENDING;

    @Column(name = "review_comment", columnDefinition = "TEXT")
    private String reviewComment;

    @Column(name = "reviewed_by")
    private String reviewedBy;

    @OneToMany(mappedBy = "companyDetails")
    @JsonIgnore
    private List<DocumentDetails> documentDetails;

}


