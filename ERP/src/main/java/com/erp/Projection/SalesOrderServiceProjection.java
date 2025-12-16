package com.erp.Projection;

import java.math.BigDecimal;

public interface SalesOrderServiceProjection {

    Long getServiceId();
    BigDecimal getQuantity();
    BigDecimal getSubtotal();
    BigDecimal getTaxAmount();
    BigDecimal getTotalAmount();
}
