package com.erp.Service.ServiceType;

import com.erp.Dto.Request.CommanParam;
import com.erp.Dto.Request.ServiceRequest;
import com.erp.Dto.Response.ServiceResponse;

import java.util.List;

public interface ServiceType {

    ServiceResponse addService(ServiceRequest serviceRequest);

    ServiceResponse updateById(ServiceRequest serviceRequest);

    List<ServiceResponse> findByIdOrServiceName(CommanParam param);

    ServiceResponse deleteByServiceId(CommanParam param);

    List<ServiceResponse> fetchAllServices();

    List<ServiceResponse> findByStatus(ServiceRequest serviceRequest);

    List<String> fetchAllCategories();

    List<ServiceResponse> fetchServiceByCategory(ServiceRequest serviceRequest);



}
