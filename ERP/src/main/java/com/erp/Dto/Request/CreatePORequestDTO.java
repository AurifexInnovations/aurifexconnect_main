package com.erp.Dto.Request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CreatePORequestDTO {

    @NotNull
    private Long vendorId;

    @NotNull
    private LocalDate dateIssued;

    @NotNull
    private LocalDate expectedDeliveryDate;

    private BigDecimal shippingCost;

    @NotEmpty
    private List<LineItemRequestDTO> lineItems;
}
