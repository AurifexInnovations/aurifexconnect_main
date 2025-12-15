package com.erp.Dto.Request;

import com.erp.Dto.Constraints.ServiceDescriptionFormat;
import com.erp.Dto.Constraints.ServiceNameFormat;
import com.erp.Enum.ServiceCategory;
import com.erp.Enum.ServiceStatus;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ServiceRequest {

    private long serviceId;

    @ServiceNameFormat
    private String serviceName;

    @ServiceDescriptionFormat
    private String serviceDescription;

    @Positive(message = "Service price must be positive")
    private BigDecimal servicePrice;

    @Positive(message = "Service residential price must be positive")
    private BigDecimal residentialPrice;

    @Positive(message = "Service commercial price must be positive")
    private BigDecimal commercialPrice;

    @NotNull(message = "Service status cannot be null")
    private ServiceStatus serviceStatus;

    @NotNull(message = "Service category cannot be null")
    private ServiceCategory serviceCategory;

    List<ServiceDocumentRequestDto> documents;

    private long branchId;

    private List<Long> inventoryIds;
}
