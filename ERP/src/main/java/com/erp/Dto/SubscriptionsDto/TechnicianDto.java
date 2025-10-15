package com.erp.Dto.SubscriptionsDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TechnicianDto {

    private Long technicianId;
    private String technicianName;
    private String contact;
    private Integer age;
    private String gender;
    private String branchCode;
    private String activeYn;
//    private String createdBy;
//    private Timestamp createdOn;
//    private String deletedBy;
//    private Timestamp deletedOn;
}