package com.erp.Service.Amc;

import com.erp.Dto.Request.AmcRequestDto;
import com.erp.Dto.Request.SalesOrderRequestDto;
import com.erp.Dto.Response.AmcResponseDto;
import com.erp.Dto.Response.ResultDto;
import com.erp.Model.SalesOrder;

public interface AmcService {

    public void createFromSalesOrder(SalesOrder salesOrder, SalesOrderRequestDto salesOrderRequestDto);

    ResultDto<AmcResponseDto> getAllAmc();

    AmcResponseDto getAmcById(Long amcId);

    AmcResponseDto updateAmcById(AmcRequestDto requestDto);

    ResultDto<AmcResponseDto> getAllByBranchId(Long branchId);
}
