package com.erp.Dto.Request;

import com.erp.Enum.ServiceCategory;
import com.erp.Model.Service;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeadResponse {

    private Long id;

    private String leadName;

    private String companyName;

    private String email;
    private String phone;
    private String source;

    private String typeOfLead;

    private String leadStatus;

    private Integer engagementScore;

    private String remarks;

    private List<LeadProductRequestDto> products;

    private String lostReason;
    private ServiceCategory serviceCategory;
    private double sqrt;
    private List<Long> services;
}
