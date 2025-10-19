package com.erp.Projection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialUsageDto {
    private String productName;
    private Double quantity;
    private String unit;
}
