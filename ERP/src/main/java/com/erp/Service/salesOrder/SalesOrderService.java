package com.erp.Service.salesOrder;



import com.erp.Dto.Request.SalesOrderRequestDto;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.SalesOrderResponseDto;

import java.util.List;

public interface SalesOrderService {

    SalesOrderResponseDto create(SalesOrderRequestDto requestDto);

    SalesOrderResponseDto update(Long id, SalesOrderRequestDto requestDto);

    SalesOrderResponseDto getById(Long id);

    List<SalesOrderResponseDto> getAll();

    void delete(Long id);

    ResultDto<SalesOrderResponseDto> getAllByBranchId(Long branchId);
}
