package com.erp.Dto.Request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SaledOrderProductRequestDto {


    private Long serviceOrProductId;

    private BigDecimal quantity;

    private BigDecimal subtotal;

    private BigDecimal taxAmount;

    private BigDecimal totalAmount;
}
