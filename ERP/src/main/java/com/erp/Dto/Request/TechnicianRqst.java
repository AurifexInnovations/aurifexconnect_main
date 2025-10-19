package com.erp.Dto.Request;

import lombok.Data;

@Data
public class TechnicianRqst {
    private String technicianName;
    private String contact;
    private Integer age;
    private String gender;
    private String branchCode;
    private String createdBy;
}
