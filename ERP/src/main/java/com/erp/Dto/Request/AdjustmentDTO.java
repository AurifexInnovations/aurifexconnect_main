package com.erp.Dto.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdjustmentDTO {
    private Long invoiceAndBillId;       // or billId depending on type
    private double adjustAmount;
}
