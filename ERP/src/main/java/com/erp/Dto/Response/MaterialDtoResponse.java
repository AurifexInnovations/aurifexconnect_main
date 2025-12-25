package com.erp.Dto.Response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaterialDtoResponse {
    private Long materialId;
    private boolean isUsed;
    private String materialName;
    private Double materialQuantity;
    private String materialUnit;
}
