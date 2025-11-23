package com.erp.Dto.Response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DebitNoteResponseDTO {

    private Long dnId;
    private Long billId;
    private Long vendorId;
    private String dateIssued;
    private String reason;
    private BigDecimal amountDebited;
    private String dnNumber;
    private Boolean inventoryAdjustment;
    private BigDecimal taxAdjustmentAmount;
    private String status;
}
