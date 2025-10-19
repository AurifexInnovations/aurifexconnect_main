package com.erp.Projection;

import java.math.BigDecimal;

public interface TechnicianLeaderboardProjection {
    Long getTechnicianId();
    String getTechnicianName();
    Long getCompletedTasks();
    BigDecimal getAvgRating();
    Integer getRank();
}
