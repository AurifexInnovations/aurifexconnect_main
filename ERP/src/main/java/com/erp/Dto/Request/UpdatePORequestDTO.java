package com.erp.Dto.Request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class UpdatePORequestDTO {

    private Long vendorId;

    private LocalDate dateIssued;

    private LocalDate expectedDeliveryDate;

    private BigDecimal shippingCost;

    private List<LineItemRequestDTO> lineItems;
}
