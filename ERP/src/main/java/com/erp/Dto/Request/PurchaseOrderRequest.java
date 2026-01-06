package com.erp.Dto.Request;

import com.erp.Enum.PurchaseOrderStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PurchaseOrderRequest {
    private Long vendorId;
    private Long customerId;
    private Long branchId;
    private LocalDate poDate;
    private LocalDate deliveryDate;
    private Long inventory_id;
    private Double discount;
    private PurchaseOrderStatus status;
}
