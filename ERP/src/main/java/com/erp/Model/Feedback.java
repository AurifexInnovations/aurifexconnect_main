package com.erp.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "feedbacks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name =  "customer_id")
    private long customerId;
    @Column(name =  "task_id")
    private long taskId;
    @Column(name =  "rating")
    private float rating;
    @Column(name =  "comment")
    private String comment;
    @Column(name =  "is_active")
    private boolean isActive;
    @Column(name =  "created_at")
    private LocalDateTime createdAt;
    @Column(name =  "updated_at")
    private LocalDateTime updatedAt;
}
