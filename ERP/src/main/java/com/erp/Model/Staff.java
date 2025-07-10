package com.erp.Model;

import com.erp.Enum.Designation;
import com.erp.Enum.StaffStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String staffName;
    private String email;
    private String contactNo;

    @Enumerated(EnumType.STRING)
    private Designation designation; // Example: Manager, Salesperson, HR

    @Enumerated(EnumType.STRING)
    private StaffStatus staffStatus;

    @CreatedDate
    private LocalDateTime createdAt;

    // Relation with Branch
    @ManyToOne
    private Branch branch;

}
