package com.erp.Dto.Response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CustomerResponse {

    private Long id;
    private String customerName;
    private String companyName;
    private String email;
    private String phone;
    private String addressLine1;
    private String addressLine2;
    private String landmark;
    private String city;
    private String state;
    private String country;
    private String pincode;
    private String tags;
    private String customerStatus;
    private LocalDate joinedDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ProductDetailDto> productDetailDto;
}
