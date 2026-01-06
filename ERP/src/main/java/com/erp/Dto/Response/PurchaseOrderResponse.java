package com.erp.Dto.Response;

import com.erp.Enum.PurchaseOrderStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PurchaseOrderResponse {

    private Long po_id;
    private Long vendor_id;
    private Long customer_id;
    private Long branch_id;

    private LocalDate date;
    private LocalDate delivery_date;
    private PurchaseOrderStatus status;
    private Double total_value;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    private List<PurchaseOrderItemDTO> items;
}
