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
    @Column(name = "id")
    private long id;

    @Column(name = "staff_name")
    private String staffName;

    @Column(name = "email")
    private String email;

    @Column(name = "contact_no")
    private String contactNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "designation")
    private Designation designation;

    @Enumerated(EnumType.STRING)
    @Column(name = "staff_status")
    private StaffStatus staffStatus;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    private Branch branch;
}
