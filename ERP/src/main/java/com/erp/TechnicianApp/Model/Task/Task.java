package com.erp.TechnicianApp.Model.Task;


import com.erp.TechnicianApp.Enum.TaskStatus;
import com.erp.TechnicianApp.Model.Technician.Technician;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private long taskId;

    private String clientName;
    private String clientLocation;
    private String serviceType;
    private LocalDateTime scheduleTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status = TaskStatus.PENDING;

    private String feedback;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime lastUpdatedAt;

    @ManyToOne(optional = true) // allow null
    @JoinColumn(name = "technician_id", nullable = true)
    private Technician technician;
}
