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
    @Column(name = "performance_id", updatable = false)
    private Long performanceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // ✅ explicit join column
    private User user;

    @Column(name = "month", nullable = false)
    private Integer month;

    @Column(name = "year", nullable = false)
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
}
