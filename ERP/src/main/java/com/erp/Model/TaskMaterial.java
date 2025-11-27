package com.erp.Model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "task_material")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_material_id")
    private Long taskMaterialId;

    @Column(name = "task_id", nullable = false)
    private Long taskId;

    @Column(name = "material_id", nullable = false)
    private Long materialId;

    @Column(name = "unit", length = 50)
    private String unit;


    @Column(name ="is_used" )
    private  Boolean isUsed;

    @Column(name ="quantity" )
    private Double quantity;



}
