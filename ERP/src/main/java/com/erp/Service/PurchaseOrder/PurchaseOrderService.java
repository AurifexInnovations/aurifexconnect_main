package com.erp.Service.PurchaseOrder;

import com.erp.Dto.Request.CreatePORequestDTO;
import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Request.UpdatePORequestDTO;
import com.erp.Dto.Response.PurchaseOrderResponseDTO;
import com.erp.Dto.Response.ResultDto;

public interface PurchaseOrderService {

    PurchaseOrderResponseDTO createPO(CreatePORequestDTO dto);

    ResultDto<PurchaseOrderResponseDTO> filterPurchaseOrders(FilterRequest filterRequest);

    PurchaseOrderResponseDTO getPODetail(Long id);

    PurchaseOrderResponseDTO updateStatus(Long id, String status);

    public PurchaseOrderResponseDTO updatePO(Long id, UpdatePORequestDTO dto);

}
