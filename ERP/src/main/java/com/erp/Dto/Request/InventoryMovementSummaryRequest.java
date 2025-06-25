package com.erp.Dto.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryMovementSummaryRequest {
    private String type;  // "day", "week", "month"
}