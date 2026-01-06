package com.erp.Service.PurchaseOrder;

import com.erp.Dto.Request.PurchaseOrderRequest;
import com.erp.Dto.Response.PurchaseOrderResponse;

import java.util.List;

public interface PurchaseOrderService {
    PurchaseOrderResponse create(PurchaseOrderRequest request);
    List<PurchaseOrderResponse> getAll();
    PurchaseOrderResponse getById(Long id);
    PurchaseOrderResponse update(Long id, PurchaseOrderRequest request);
    void delete(Long id);
}
