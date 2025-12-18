package com.erp.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CalculationVar {

    private BigDecimal total;
    private BigDecimal taxAmount;
    private BigDecimal subTotal;
    private BigDecimal grandTotal;

}
