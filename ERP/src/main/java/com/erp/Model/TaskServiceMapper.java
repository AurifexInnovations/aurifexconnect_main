package com.erp.Model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "task_services")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskServiceMapper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_service_id")
    private Long taskServiceId;

    @Column(name = "task_id", nullable = false)
    private Long taskId;

    @Column(name = "service_id", nullable = false)
    private Long serviceId;


}
