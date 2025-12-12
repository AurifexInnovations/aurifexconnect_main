package com.erp.Dto.Request;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDetailsRequestDto {

    private Long id; // for update
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
    private List<CustomerMapperRequestDto> products; // product-service mapping

    private List<Long> services;
    private String alternatePhone;
    private String locationUrl;
    private String customerType;
    private String serviceCategory;
    private Double sqrt;
    private long totalQuotation;
    private long totalSalesOrder;
    private long totalInvoices;

    private long leadId;
}
