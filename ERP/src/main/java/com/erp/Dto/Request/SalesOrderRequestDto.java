package com.erp.Dto.Request;

import com.erp.Enum.SalesOrderStatus;
import com.erp.Enum.SalesOrderType;
import com.erp.Enum.ServiceCategory;

import jakarta.persistence.Column;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class SalesOrderRequestDto {

    private Long quotationId;
    private Long customerId;
    private Long branchId;

    private String phoneNumber;
    private String alternatePhoneNumber;

    private LocalDate salesOrderDate;

    private String customerName;
    private String companyName;
    private String email;

    private String addressLine1;
    private String addressLine2;
    private String landmark;
    private String city;
    private String state;
    private String country;
    private String pincode;

    private String locationUrl;

//    private String serviceCategory;

    private BigDecimal sqft;

    private SalesOrderType soType;
    private SalesOrderStatus status;

    private BigDecimal subtotal;

    private BigDecimal taxAmount;

    private BigDecimal discountPrice;
//
    private BigDecimal totalAmount ;

    private BigDecimal grandTotal;

    private String notes;
    private ServiceCategory serviceType;

    private List<SaledOrderProductRequestDto> salesOrderRequest;
}
