package com.erp.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
public class ServiceTypeResponse {
    private Long serviceId;
    private String serviceName;
    private String serviceDescription;
    private Double servicePrice;
    private String serviceStatus;
    private String serviceCategory;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    public ServiceTypeResponse(Long serviceId, String serviceName, String serviceDescription,
                               Double servicePrice, String serviceStatus, String serviceCategory,
                               LocalDateTime createdAt, LocalDateTime lastModifiedAt) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceDescription = serviceDescription;
        this.servicePrice = servicePrice;
        this.serviceStatus = serviceStatus;
        this.serviceCategory = serviceCategory;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
    }
}
