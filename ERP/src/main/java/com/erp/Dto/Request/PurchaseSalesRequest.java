package com.erp.Dto.Request;

import com.erp.Enum.VoucherType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseSalesRequest {
    private String type; // day, week, month
    private VoucherType voucherType; // PURCHASE or SALES
}
