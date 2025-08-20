package com.erp.Dto.Request;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DealRequest {
    private String title;
    private BigDecimal amount;
    private LocalDate closeDate;
    private Long contactId;
    private Long accountId;
    private Long assignedToId;
    private String assignedToName;
}