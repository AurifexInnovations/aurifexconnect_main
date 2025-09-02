package com.erp.TechnicianApp.TechnicianModel;

import com.erp.Model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Table(name = "support_requests")
public class CustomerSupport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long supportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "technician_id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "task_id")
    private TechnicianTask task;

    private String customerName;
    private String customerContact;

    @Column(columnDefinition = "TEXT")
    private String issueDescription;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime createdAt;
    private LocalDateTime resolvedAt;

    private String feedback;

    public enum Priority { LOW, MEDIUM, HIGH, CRITICAL }
    public enum Status { OPEN, IN_PROGRESS, RESOLVED, CLOSED }

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
