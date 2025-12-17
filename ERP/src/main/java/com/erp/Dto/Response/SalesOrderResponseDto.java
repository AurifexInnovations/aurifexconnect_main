package com.erp.Dto.Response;

import com.erp.Enum.SalesOrderStatus;
import com.erp.Enum.SalesOrderType;
import com.erp.Enum.ServiceCategory;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class SalesOrderResponseDto {

    private Long salesOrderNumber;

    private Long quotationId;
    private Long customerId;

    private String phoneNumber;
    private String customerName;
    private String companyName;
    private String email;

    private LocalDate salesOrderDate;

    private SalesOrderType soType;
    private SalesOrderStatus status;
    private ServiceCategory serviceType;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
