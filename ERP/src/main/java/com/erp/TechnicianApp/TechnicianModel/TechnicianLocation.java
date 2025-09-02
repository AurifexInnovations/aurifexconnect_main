package com.erp.TechnicianApp.TechnicianModel;

import com.erp.Model.Attendance;
import com.erp.Model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "technician_location")
@Getter
@Setter
@NoArgsConstructor
public class TechnicianLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Long locationId;

    /** 🔹 Linked technician (User) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /** 🔹 Attendance session (check-in/out) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendance_id")
    private Attendance attendance;

    /** 🔹 Task being worked on (optional) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private TechnicianTask task;

    /** 🔹 GPS Data */
    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "altitude")
    private Double altitude;

    @Column(name = "speed")
    private Float speed;

    @Column(name = "bearing")
    private Float bearing;

    @Column(name = "accuracy")
    private Float accuracy;

    @Column(name = "provider")
    private String provider;

    /** 🔹 Device & Notes */
    @Column(name = "battery_level")
    private Integer batteryLevel;

    @Column(name = "note")
    private String note;

    /** 🔹 Image proof (optional) */
    @Column(name = "image_url")
    private String imageUrl;

    /** 🔹 Timestamp */
    @Column(name = "recorded_at")
    private LocalDateTime recordedAt;
}