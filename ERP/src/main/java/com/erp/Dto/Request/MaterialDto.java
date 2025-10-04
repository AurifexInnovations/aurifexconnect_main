package com.erp.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaterialDto {


    private Long materialId;
    private Double unit;
    private Double quantity;
    private  Boolean isUsed;

}
