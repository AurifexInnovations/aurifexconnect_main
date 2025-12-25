package com.erp.Dto.Response;

import com.erp.Enum.SalesOrderStatus;
import com.erp.Enum.SalesOrderType;
import com.erp.Enum.ServiceCategory;

import jakarta.persistence.Column;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class SalesOrderResponseDto {

    private Long salesOrderNumber;

    private Long quotationId;
    private Long customerId;
    private Long branchId;

    private String phoneNumber;
    private String alternatePhoneNumber;
    private String customerName;
    private String companyName;
    private String email;

    private String addressLine1;
    private String addressLine2;

    private SalesOrderType soType;
    private SalesOrderStatus status;

    private BigDecimal subtotal;

    private BigDecimal taxAmount;

    private BigDecimal discountPrice ;

    private BigDecimal totalAmount;

    private BigDecimal grandTotal;
    private ServiceCategory serviceType;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
