package com.erp.TechnicianApp.TechnicianModel;

import com.erp.Model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "technician_performance")
@Getter
@Setter
public class TechnicianPerformance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "performance_id")
    private Long performanceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "month")
    private Integer month;

    @Column(name = "year")
    private Integer year;

    @Column(name = "punctuality_score")
    private BigDecimal punctualityScore;

    @Column(name = "task_completion_score")
    private BigDecimal taskCompletionScore;

    @Column(name = "overtime_score")
    private BigDecimal overtimeScore;

    @Column(name = "customer_feedback_score")
    private BigDecimal customerFeedbackScore;

    @Column(name = "final_rating")
    private BigDecimal finalRating;


    public enum TechnicianStatus {
        ACTIVE,
        IDLE,
        OFFLINE
    }
}
