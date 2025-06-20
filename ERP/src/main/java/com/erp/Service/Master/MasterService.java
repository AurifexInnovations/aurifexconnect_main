package com.erp.Service.Master;

import com.erp.Dto.Request.MasterRequest;
import com.erp.Dto.Response.MasterResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface MasterService {

    MasterResponse createMaster(MasterRequest masterRequest);

    MasterResponse findById(MasterRequest masterRequest);
    double getTotalPurchaseAmount(LocalDate startDate, LocalDate endDate);
    List<Map<String, Object>> getSalesVsPurchaseComparison(String type);

//    MasterResponse updateMaster(Long masterId, MasterRequest masterRequest);
//
//    MasterResponse deleteMaster(Long masterId);
}