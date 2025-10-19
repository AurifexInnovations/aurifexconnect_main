package com.erp.Dto.Request;

import com.erp.Dto.Constraints.ServiceDescriptionFormat;
import com.erp.Dto.Constraints.ServiceNameFormat;
import com.erp.Enum.ServiceCategory;
import com.erp.Enum.ServiceStatus;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceRequest {

    private long serviceId;

    @ServiceNameFormat
    private String serviceName;

    @ServiceDescriptionFormat
    private String serviceDescription;

    @Positive(message = "Service price must be positive")
    private double servicePrice;

    @NotNull(message = "Service status cannot be null")
    private ServiceStatus serviceStatus;

    @NotNull(message = "Service category cannot be null")
    private ServiceCategory serviceCategory;
}
