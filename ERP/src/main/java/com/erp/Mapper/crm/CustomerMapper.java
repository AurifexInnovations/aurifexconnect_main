package com.erp.Mapper.crm;

import com.erp.Dto.Request.CustomerDetailsRequestDto;
import com.erp.Dto.Request.CustomerRequestDto;
import com.erp.Dto.Response.CustomerResponse;
import com.erp.Dto.Response.CustomerResponseDto;
import com.erp.Model.Customer;
import com.erp.Model.CustomerDetails;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    Customer toEntity(CustomerRequestDto dto);

    CustomerResponseDto toDto(Customer entity);

    List<CustomerResponseDto> toDtoList(List<Customer> entities);

    CustomerDetails toCustomerDetails(CustomerDetailsRequestDto customerDetailsRequestDto);

    CustomerResponse toCustomerResponseDto(CustomerDetails customerDetails);
}