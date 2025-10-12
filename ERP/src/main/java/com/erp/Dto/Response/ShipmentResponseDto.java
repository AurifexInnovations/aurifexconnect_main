package com.erp.Dto.Response;

import com.erp.Dto.Request.ShipmentDetailsRequestDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentResponseDto {

    private String message;
    private ShipmentDetailsRequestDto dto;

}
