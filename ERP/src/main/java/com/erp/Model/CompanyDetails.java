package com.erp.Model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.erp.Enum.IndustryType;
import com.erp.Enum.ReviewStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "company_details")
@EntityListeners(AuditingEntityListener.class)
public class CompanyDetails extends BaseEntity {

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

    @OneToMany(mappedBy = "companyDetails",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<DocumentDetails> documentDetails;

}


