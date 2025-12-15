package com.erp.Service.Cutomer;

import com.erp.Dto.Request.CustomerDetailsRequestDto;
import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.CustomerResponse;
import com.erp.Dto.Response.CustomerResponseDtos;
import com.erp.Dto.Response.ResultDto;


public interface CustomerDetailsService {
    CustomerResponseDtos addOrUpdateCustomer(CustomerDetailsRequestDto request);

    ResultDto<CustomerResponse> getFilteredCustomers(FilterRequest filterRequest);

    CustomerResponse addCustomer(CustomerDetailsRequestDto customerDetailsRequestDto);

    ResultDto<CustomerResponse> getAll();

    CustomerResponse getById(long id);

    CustomerResponse deleteById(long id);

    ResultDto<CustomerResponse> getByBranchWise();
}
