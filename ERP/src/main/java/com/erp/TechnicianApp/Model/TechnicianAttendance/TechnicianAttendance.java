package com.erp.TechnicianApp.Model.TechnicianAttendance;

import com.erp.TechnicianApp.Enum.AttendanceStatus;
import com.erp.TechnicianApp.Enum.TechnicianDayStatus;
import com.erp.TechnicianApp.Model.Task.Task;
import com.erp.TechnicianApp.Model.Technician.Technician;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "technician_attendance")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class TechnicianAttendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    private Long attendanceId;

    @Column(name = "check_in_time")
    private LocalDateTime checkInTime;

    @Column(name = "check_out_time")
    private LocalDateTime checkOutTime;

    @Column(name = "check_in_latitude")
    private Double checkInLatitude;

    @Column(name = "check_in_longitude")
    private Double checkInLongitude;

    @Column(name = "check_out_latitude")
    private Double checkOutLatitude;

    @Column(name = "check_out_longitude")
    private Double checkOutLongitude;

    @Column(name = "notes", length = 500)
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_status", nullable = false)
    private TechnicianDayStatus dayStatus = TechnicianDayStatus.ABSENT;

    @Enumerated(EnumType.STRING)
    @Column(name = "attendance_status", nullable = false)
    private AttendanceStatus status = AttendanceStatus.CHECK_OUT;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "technician_id", nullable = false)
    private Technician technician;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task; // null if marking daily attendance only

}
