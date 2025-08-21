package com.erp.TechnicianApp.Model.Technician;


import com.erp.TechnicianApp.Enum.TechnicianDayStatus;
import com.erp.TechnicianApp.Model.Task.Task;
import com.erp.TechnicianApp.Model.TechnicianAttendance.TechnicianAttendance;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Technician {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long technicianId;

    private String technicianName;
    private String email;
    private int age;

    private String role;
    private String mobileNumber;
    private String panNumber;
    private String password;

    @Enumerated(EnumType.STRING)
    private TechnicianDayStatus status;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime lastUpdateAt;

    @OneToMany(mappedBy = "technician", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Task> tasks = new ArrayList<>();

    @OneToMany(mappedBy = "technician", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TechnicianAttendance> attendances = new ArrayList<>();

}
