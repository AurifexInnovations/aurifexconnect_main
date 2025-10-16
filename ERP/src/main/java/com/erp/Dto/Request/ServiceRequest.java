package com.erp.Dto.Request;

import com.erp.Enum.ServiceStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceRequest {

    private String serviceName;

    private String serviceDescription;

    private double servicePrice;

    private ServiceStatus serviceStatus;

    private String categories;

    private long id;
}
