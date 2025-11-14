package com.erp.Mapper;

import com.erp.Dto.Request.CustomerDetailsRequestDto;
import com.erp.Model.CustomerDetails;


public class CustomerMapper {

    public static CustomerDetails toEntity(CustomerDetailsRequestDto dto) {
        return CustomerDetails.builder()
                .id(dto.getId())
                .customerName(dto.getCustomerName())
                .companyName(dto.getCompanyName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .addressLine1(dto.getAddressLine1())
                .addressLine2(dto.getAddressLine2())
                .landmark(dto.getLandmark())
                .city(dto.getCity())
                .state(dto.getState())
                .country(dto.getCountry())
                .pincode(dto.getPincode())
                .tags(dto.getTags())
                .customerStatus(dto.getCustomerStatus())
                .build();
    }
}
