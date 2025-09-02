package com.erp.TechnicianApp.TechnicianModel;

import com.erp.Model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "technician_task")
@Getter
@Setter
public class TechnicianTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long taskId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "title")
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TechnicianTaskStatus status = TechnicianTaskStatus.PENDING;

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "feedback")
    private String feedback;

    @Column(name = "client_name")
    private String clientName;

    @Column(name = "client_location")
    private String clientLocation;

    @Column(name = "service_type")
    private String serviceType;

    @Column(name = "scheduled_at")
    private LocalDateTime scheduledAt;

    @Column(name = "scheduled_for")
    private LocalDateTime scheduledFor;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "job_latitude")
    private Double jobLatitude;

    @Column(name = "job_longitude")
    private Double jobLongitude;

    @Column(name = "job_radius_meters")
    private Integer jobRadiusMeters;

    public enum TechnicianTaskStatus {
        PENDING,
        IN_PROGRESS,
        COMPLETED,
        FAILED
    }
}