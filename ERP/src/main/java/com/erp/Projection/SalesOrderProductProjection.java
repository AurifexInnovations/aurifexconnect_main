package com.erp.Projection;

import java.math.BigDecimal;

public interface SalesOrderProductProjection {

    Long getProductId();
    BigDecimal getQuantity();
    BigDecimal getSubtotal();
    BigDecimal getTaxAmount();
    BigDecimal getTotalAmount();
}
