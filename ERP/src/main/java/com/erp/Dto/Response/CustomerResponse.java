package com.erp.Dto.Response;

import com.erp.Dto.Request.CustomerMapperRequestDto;
import com.erp.Model.Branch;
import com.erp.Model.CustomerDetailsMapper;
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
    private List<CustomerMapperRequestDto> products;

    // ---- Added to match entity (ALTER columns) ----
    private String alternatePhone;
    private String locationUrl;
    private String customerType;
    private String serviceCategory;
    private Double sqrt;
    private Long totalQuotation;
    private Long totalSalesOrder;
    private Long totalInvoices;

    // service list (from mapper table)
    private List<Long> services;
    private long branchId;
}
