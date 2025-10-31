package com.erp.Service.ServiceType;

import com.erp.Dto.Request.CommanParam;
import com.erp.Dto.Request.ServiceRequest;
import com.erp.Dto.Request.ServiceTypeGetRequest;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.ServiceResponse;
import com.erp.Dto.Response.ServiceTypeResponse;
import com.erp.Model.Service;
import com.erp.Dto.Request.FilterRequest;

import java.util.List;

public interface ServiceType {

    ServiceResponse addService(ServiceRequest serviceRequest);

    ServiceResponse updateById(ServiceRequest serviceRequest);

    List<ServiceResponse> findByIdOrServiceName(CommanParam param);

    ServiceResponse deleteByServiceId(CommanParam param);

    List<ServiceResponse> fetchAllServices();

    List<ServiceResponse> findByStatus(ServiceRequest serviceRequest);

    List<String> fetchAllCategories();

    List<ServiceResponse> findByServiceCategory(ServiceRequest serviceRequest);

    List<Service>  getAllServicesByIds(List<Long> ids);

    public ResultDto<ServiceTypeResponse> getAllServices(ServiceTypeGetRequest request) ;

    ResultDto<ServiceResponse> getAllServicesByFilter(FilterRequest filterRequest);
}
