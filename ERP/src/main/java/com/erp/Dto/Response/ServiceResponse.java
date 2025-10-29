package com.erp.Dto.Response;


import com.erp.Enum.ServiceCategory;
import com.erp.Enum.ServiceStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
public class ServiceResponse
{
    private Long serviceId;
    private String serviceName;
    private String serviceDescription;
    private double servicePrice;
    private ServiceStatus serviceStatus;
    private ServiceCategory serviceCategory;
}
