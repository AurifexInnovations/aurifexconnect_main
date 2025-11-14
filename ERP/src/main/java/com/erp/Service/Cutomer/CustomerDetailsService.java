package com.erp.Service.Cutomer;

import com.erp.Dto.Request.CustomerDetailsRequestDto;
import com.erp.Dto.Response.CustomerResponseDtos;


public interface CustomerDetailsService {
    CustomerResponseDtos addOrUpdateCustomer(CustomerDetailsRequestDto request);
}
