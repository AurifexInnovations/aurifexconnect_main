package com.erp.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InventoryMovementSummaryResponse {
    private String period;   // e.g. "2025-06-19" or "2025-W25" or "2025-06"
    private double totalQuantity;
}
