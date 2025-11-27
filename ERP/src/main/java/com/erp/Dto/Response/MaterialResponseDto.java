package com.erp.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialResponseDto {

    private Long materialId;
    private Double quantity;
    private String materialName;
    private String unit;
}
