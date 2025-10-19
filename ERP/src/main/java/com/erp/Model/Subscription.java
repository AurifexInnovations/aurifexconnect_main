package com.erp.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Subscription")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {

    @Id
    @Column(name = "subscriptionId")
    private Long subscriptionId;

    @Column(name = "userId", nullable = false)
    private String userId;

    @Column(name = "subscriptionPlan", nullable = false)
    private String subscriptionPlan;

    @Column(name = "planPeriod", nullable = false)
    private Long planPeriod;

    @Column(name = "planStartDate", nullable = false)
    private LocalDateTime planStartDate;

    @Column(name = "planEndDate", nullable = false)
    private LocalDateTime planEndDate;

    @Column(name = "activeYn", length = 1)
    private String activeYn;

    @Column(name = "createdBy")
    private String createdBy;

    @Column(name = "createdOn", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdOn;

    @Column(name = "updatedBy")
    private String updatedBy;

    @Column(name = "updatedOn", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedOn;

    @Column(name = "deletedBy")
    private String deletedBy;

    @Column(name = "deletedOn", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime deletedOn;

    // Getters and setters omitted for brevity
}