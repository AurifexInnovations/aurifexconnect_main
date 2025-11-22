package com.erp.Dto.Response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class PurchaseOrderResponseDTO {

    private Long poId;
    private Long vendorId;
    private String vendorName;

    private String status;
    private String poNumber;

    private LocalDate dateIssued;
    private LocalDate expectedDeliveryDate;

    private BigDecimal shippingCost;
    private BigDecimal totalValue;

    private List<LineItemResponseDTO> lineItems;
}
