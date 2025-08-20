package com.erp.Dto.Response;

import com.erp.Enum.DealStage;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class DealResponse {
    private Long id;
    private String title;
    private BigDecimal amount;
    private DealStage stage;
    private LocalDate closeDate;
    private Long contactId;
    private Long accountId;
    private Long assignedToId;
    private String assignedToName;
    private LocalDateTime createdAt;
}
