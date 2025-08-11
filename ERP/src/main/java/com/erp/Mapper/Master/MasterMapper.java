package com.erp.Mapper.Master;

import com.erp.Dto.Request.MasterRequest;
import com.erp.Dto.Response.MasterResponse;
import com.erp.Model.Master;


public interface MasterMapper {

    Master mapToMasterRequest(MasterRequest masterRequest);

    void mapToMasterEntity(MasterRequest masterRequest, Master master);

    MasterResponse mapToMasterResponse(Master master);
}
