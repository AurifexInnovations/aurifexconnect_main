package com.erp.Model;

import com.erp.Enum.FieldType;
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

    @Column(name = "task_id", nullable = false)
    private Long taskId;

    @Column(name = "assigned_date")
    private LocalDate assignedDate;

    @Column(name = "assigned_time")
    private LocalTime assignedTime;

    @Column(name = "google_location_link", columnDefinition = "TEXT")
    private String googleLocationLink;


    @Enumerated(EnumType.STRING)
    @Column(name = "field_type")
    private FieldType fieldType;

    @Column(name = "service_location", columnDefinition = "TEXT")
    private String serviceLocation;

    @Column(name = "feedback_id")
    private Long feedbackId;

    private LocalTime taskStartTime;

    private LocalTime taskEndTime;



}
