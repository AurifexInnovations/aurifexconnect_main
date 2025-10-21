package com.erp.Projection;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TaxProjection {
    private Long id;
    private String taxName;
    private String taxType;
    private BigDecimal taxRate;
    private LocalDateTime createdAt;
}
