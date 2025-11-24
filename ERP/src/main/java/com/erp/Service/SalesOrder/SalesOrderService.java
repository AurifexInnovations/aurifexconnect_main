package com.erp.Service.SalesOrder;

import com.erp.Dto.Request.CreateSORequestDTO;
import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Request.UpdateSORequestDTO;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.SalesOrderResponseDTO;

public interface SalesOrderService {

    SalesOrderResponseDTO createSO(CreateSORequestDTO dto);

    ResultDto<SalesOrderResponseDTO> filterSalesOrders(FilterRequest filterRequest);

    SalesOrderResponseDTO getSODetail(Long id);

    SalesOrderResponseDTO updateStatus(Long id, String status);

    SalesOrderResponseDTO updateSO(Long id, UpdateSORequestDTO dto);

    public void deactivateSO(Long soId);
}
