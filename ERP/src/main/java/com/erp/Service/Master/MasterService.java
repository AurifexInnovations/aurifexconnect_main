package com.erp.Service.Master;

import com.erp.Dto.Request.MasterRequest;
import com.erp.Dto.Request.PurchaseSalesRequest;
import com.erp.Dto.Response.MasterResponse;
import com.erp.Dto.Response.PurchaseSalesResponse;

import java.util.List;

public interface MasterService {

    MasterResponse createMaster(MasterRequest masterRequest);

    MasterResponse findById(MasterRequest masterRequest);

//    MasterResponse updateMaster(Long masterId, MasterRequest masterRequest);
//
//    MasterResponse deleteMaster(Long masterId);

    List<PurchaseSalesResponse> getPurchaseSalesSummary(PurchaseSalesRequest request);
}
