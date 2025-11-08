package com.erp.Dto.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockTransferRequest
{
    private long fromBranchId;
    private long toBranchId;
    private long inventoryId;
    private double quantity;
    private String reason;
}
