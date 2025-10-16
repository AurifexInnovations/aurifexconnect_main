package com.erp.Dto.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LineItemsRequest {

    private long inventoryId;
    private long masterId;
    private long ledgerId;
    private double quantity;
}
