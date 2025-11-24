package com.erp.Dto.Response;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class SalesOrderLineItemResponseDTO {

    private Long productId;        // product_id
    private BigDecimal quantity;   // quantity
    private BigDecimal unitPrice;  // unit_price
    private BigDecimal discountPercent; // discount_percent
    private BigDecimal subtotal;   // subtotal
}
