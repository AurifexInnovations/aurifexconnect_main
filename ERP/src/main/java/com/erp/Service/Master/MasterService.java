package com.erp.Service.Master;

import com.erp.Dto.Request.MasterRequest;
import com.erp.Dto.Request.PurchaseSalesRequest;
import com.erp.Dto.Response.MasterResponse;
import com.erp.Dto.Response.PurchaseSalesResponse;
import com.erp.Dto.Response.PurchaseSalesResponse;

import javax.naming.LimitExceededException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface MasterService {

    MasterResponse createMaster(MasterRequest masterRequest) throws LimitExceededException;

    MasterResponse findById(MasterRequest masterRequest);

//    MasterResponse updateMaster(Long masterId, MasterRequest masterRequest);
//
//    MasterResponse deleteMaster(Long masterId);


    //NEW UPDATE
    List<PurchaseSalesResponse> getPurchaseSalesSummary(PurchaseSalesRequest request);
}

