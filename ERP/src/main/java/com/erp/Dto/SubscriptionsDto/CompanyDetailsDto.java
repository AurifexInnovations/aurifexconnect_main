package com.erp.Dto.SubscriptionsDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDetailsDto {

    private Long companyId;
    private String companyName;
    private String companyCode;
    private String activeYn;
//    private String createdBy;
//    private Timestamp createdOn;
//    private String deletedBy;
//    private Timestamp deletedOn;
}