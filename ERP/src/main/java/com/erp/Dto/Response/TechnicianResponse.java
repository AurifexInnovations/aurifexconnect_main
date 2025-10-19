package com.erp.Dto.Response;

import lombok.Data;

@Data
public class TechnicianResponse {
    private Long technicianId;
    private String technicianName;
    private String contact;
    private String gender;
    private String activeYn;
}