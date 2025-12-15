package com.erp.Model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "follow_up_details")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class FollowUpDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lead_id")
    private Long leadId;

    @Column(name = "quotation_id")
    private Long quotationId;

    @Column(name = "follow_up_type", nullable = false)
    private String followUpType;

    @Column(nullable = false)
    private String status;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "next_followup_date")
    private LocalDate nextFollowupDate;

    @Column(name = "next_followup_time")
    private LocalTime nextFollowupTime;

    @Column(name = "lost_reason", columnDefinition = "TEXT")
    private String lostReason;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;
}

