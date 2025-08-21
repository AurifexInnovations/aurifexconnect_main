package com.erp.Service.Deal;

import com.erp.Dto.Request.DealRequest;
import com.erp.Dto.Response.DealResponse;

import java.util.List;

public interface DealService {
    DealResponse create(DealRequest request);
    DealResponse update(Long id, DealRequest request);
    DealResponse getById(Long id);
    List<DealResponse> getAll();
    void delete(Long id);
}
