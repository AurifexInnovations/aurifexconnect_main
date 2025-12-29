package com.erp.Service.Cutomer;

import com.erp.Dto.Request.CustomerDetailsRequestDto;
import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.*;
import com.erp.Model.CustomerDetails;


public interface CustomerDetailsService {
    CustomerResponseDtos addOrUpdateCustomer(CustomerDetailsRequestDto request);

    ResultDto<CustomerResponse> getFilteredCustomers(FilterRequest filterRequest);

    CustomerResponse addCustomer(CustomerDetailsRequestDto customerDetailsRequestDto);

    ResultDto<CustomerResponse> getAll();

    CustomerResponse getById(long id);

    CustomerResponse deleteById(long id);

    ResultDto<CustomerResponse> getByBranchWise();

    ResultDto<DropDown> getDropdown();

    ResultDto<TechnicianResponseDTO> findCustomerWithTask(FilterRequest filterRequest);
}
