package com.erp.Dto.Request;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LeadServiceMapperDto {
    private Long leadServiceId;
    private Long leadId;
    private Long serviceId;
}
