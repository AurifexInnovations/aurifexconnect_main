package com.erp.Dto.Response;

import com.erp.Dto.Request.InternalTicketRequestDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InternalTicketResponse {

    private  String message;
    private InternalTicketResponseDto  dto;
}
