package com.erp.Dto.Request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class BillLineItemDTO {

    private Long productId;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal taxRate;
}
