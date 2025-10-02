package com.erp.Model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "task_technicians")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TechnicianTaskMapper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_technicians_id")
    private Long taskTechniciansId;

    @Column(name = "task_id", nullable = false)
    private Long taskId;

    @Column(name = "technician_id", nullable = false)
    private Long technicianId;


}
