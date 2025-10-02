package com.erp.Model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "task_schedule")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_schedule_id")
    private Long id;

    @JoinColumn(name = "task_id", nullable = false)
    private Long taskId;

    @Column(name = "assigned_date")
    private LocalDate assignedDate;

    @Column(name = "assigned_time")
    private LocalTime assignedTime;

    @Column(name = "google_location_link", columnDefinition = "TEXT")
    private String googleLocationLink;

    @Column(name = "field_type", length = 100)
    private String fieldType;

    @Column(name = "service_location", columnDefinition = "TEXT")
    private String serviceLocation;

    @Column(name = "feedback_id")
    private Long feedbackId;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
