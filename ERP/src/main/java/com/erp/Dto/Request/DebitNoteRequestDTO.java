package com.erp.Dto.Request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DebitNoteRequestDTO {

    private Long billId;
    private String dateIssued;
    private String reason;
    private BigDecimal amountDebited;
    private Boolean inventoryAdjustment;
    private BigDecimal taxAdjustmentAmount;
}
