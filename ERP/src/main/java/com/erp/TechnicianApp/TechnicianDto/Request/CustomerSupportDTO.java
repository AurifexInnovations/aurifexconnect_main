package com.erp.TechnicianApp.TechnicianDto.Request;

import com.erp.TechnicianApp.TechnicianModel.CustomerSupport;
import lombok.Data;

@Data
public class CustomerSupportDTO {
    private Long userId;
    private String customerName;
    private String customerContact;
    private String issueDescription;
    private CustomerSupport.Priority priority;

}


