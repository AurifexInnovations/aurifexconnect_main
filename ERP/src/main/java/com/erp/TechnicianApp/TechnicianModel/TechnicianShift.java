package com.erp.TechnicianApp.TechnicianModel;

import com.erp.Model.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "technician_shifts")
public class TechnicianShift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="shift_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "shift_name")
    private String shiftName;

    @Column(name = "shift_start")
    private LocalDateTime shiftStart;

    @Column(name = "shift_end")
    private LocalDateTime shiftEnd;

    @Column(name = "shift_type")
    private String shiftType; // MORNING, NIGHT, CUSTOM

    @Column(name = "is_active")
    private boolean active;

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "end_time")
    private String endTime;
}
