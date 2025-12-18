package com.erp.Service.receipt;

import com.erp.Dto.Request.ReceiptRequestDto;
import com.erp.Dto.Response.ReceiptResponseDto;


import java.util.List;

public interface ReceiptService {

    ReceiptResponseDto create(ReceiptRequestDto requestDto);

    ReceiptResponseDto update(Long id, ReceiptRequestDto requestDto);

    List<ReceiptResponseDto> getAll();

    ReceiptResponseDto getById(Long id);

    void delete(Long id);
}
