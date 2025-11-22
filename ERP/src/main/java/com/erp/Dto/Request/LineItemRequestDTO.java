package com.erp.Dto.Request;

import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LineItemRequestDTO {

    @NotNull
    private Long productId;

    @NotNull
    private BigDecimal quantity;

    private BigDecimal unitPrice;

    private BigDecimal discountPercent;
}
