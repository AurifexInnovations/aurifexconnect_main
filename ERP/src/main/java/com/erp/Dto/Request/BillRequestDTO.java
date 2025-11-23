package com.erp.Dto.Request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class BillRequestDTO {

    @NotNull
    private Long vendorId;

    @NotNull
    private Long purchaseOrderId;

    @NotNull
    private LocalDate billDate;

    @NotNull
    private BigDecimal totalAmount;

    @NotNull
    private LocalDate dueDate;


    private String billNumber;

    private List<BillLineItemDTO> lineItems;
}
