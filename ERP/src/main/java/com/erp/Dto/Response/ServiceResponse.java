package com.erp.Dto.Response;


import com.erp.Enum.ServiceCategory;
import com.erp.Enum.ServiceStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ServiceResponse
{
    private long serviceId;
    private String serviceName;
    private String serviceDescription;
    private BigDecimal servicePrice;
    private BigDecimal residentialPrice;
    private BigDecimal commercialPrice;
    private ServiceStatus serviceStatus;
    private ServiceCategory serviceCategory;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private long branchId;
    private List<String> files;
}
