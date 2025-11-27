package com.erp.Dto.Response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaterialDtoResponse {
    private Long materialId;
    private String materialName;
    private Long materialQuantity;
    private String materialUnit;
}
