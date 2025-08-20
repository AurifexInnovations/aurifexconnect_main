package com.erp.TechnicianApp.Model.Technician;

import com.erp.TechnicianApp.Model.Task.Task;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;


@Data
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Technician {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long technicianId;
    private String technicianName;
    private String email;
    private int age;

    private String role;
    private String mobileNumber;
    private String panNumber;
    private String password;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime lastUpdateAt;

    @OneToMany(mappedBy = "technician", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks;
}
