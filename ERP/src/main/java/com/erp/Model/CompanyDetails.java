//package com.erp.Model;
//
//import org.springframework.data.jpa.domain.support.AuditingEntityListener;
//import com.erp.Enum.IndustryType;
//import com.erp.Enum.ReviewStatus;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import java.util.List;
//
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@Getter
//@Setter
//@Table(name = "company_details")
//@EntityListeners(AuditingEntityListener.class)
//public class CompanyDetails extends BaseEntity
//{
//    @Column(name = "name")
//    private String name;
//
//    @Column(name = "address")
//    private String address;
//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "industry_type")
//    private IndustryType industryType;
//
//    @Column(name = "description", columnDefinition = "TEXT")
//    private String description;
//
//    @Column(name = "gst_number")
//    private String gstNumber;
//
//    @Column(name = "pan_number")
//    private String panNumber;
//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "review_status")
//    private ReviewStatus reviewStatus = ReviewStatus.PENDING;
//
//    @Column(name = "review_comment", columnDefinition = "TEXT")
//    private String reviewComment;
//
//    @Column(name = "reviewed_by")
//    private String reviewedBy;
//
//    @OneToMany(mappedBy = "companyDetails",
//            cascade = CascadeType.ALL,
//            orphanRemoval = true)
//    private List<DocumentDetails> documentDetails;
//
//}
//
//


package com.erp.Model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.erp.Enum.IndustryType;
import com.erp.Enum.ReviewStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "company_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class CompanyDetails
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "office_no")
    private Integer officeNo;

    @Column(name = "address_line1", length = 500)
    private String addressLine1;

    @Column(name = "address_line2", length = 500)
    private String addressLine2;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "gst_number", length = 50)
    private String gstNumber;

    @Column(name = "pan_number", length = 50)
    private String panNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "industry_type")
    private IndustryType industryType;

    @Enumerated(EnumType.STRING)
    @Column(name = "review_status")
    private ReviewStatus reviewStatus = ReviewStatus.PENDING;

    @Column(name = "review_comment", columnDefinition = "TEXT")
    private String reviewComment;

    @Column(name = "contact_person_name")
    private String contactPersonName;

    @Column(name = "contact_person_email")
    private String contactPersonEmail;

    @Column(name = "contact_person_phone")
    private String contactPersonPhone;

    @Column(length = 10)
    private String pincode;

    @Column(name= "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "reviewed_by")
    private String reviewedBy;

    @OneToMany(mappedBy = "companyDetails",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<DocumentDetails> documentDetails;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_date")
    @CreatedDate
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "last_modified_by")
    private Long lastModifiedBy;

    @Column(name = "last_modified_date")
    @UpdateTimestamp
    private LocalDateTime lastModifiedDate = LocalDateTime.now();
}
