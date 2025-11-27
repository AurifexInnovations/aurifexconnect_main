package com.erp.Dto.Request;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskMaterialDTO {

    private Long materialId;

    private String unit;

    private  Boolean isUsed;

    private Double quantity;
}
