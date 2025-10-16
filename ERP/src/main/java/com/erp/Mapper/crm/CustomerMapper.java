package com.erp.Mapper.crm;

import com.erp.Dto.Request.CustomerRequestDto;
import com.erp.Dto.Response.CustomerResponseDto;
import com.erp.Model.Customer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    Customer toEntity(CustomerRequestDto dto);

    CustomerResponseDto toDto(Customer entity);

    List<CustomerResponseDto> toDtoList(List<Customer> entities);
}