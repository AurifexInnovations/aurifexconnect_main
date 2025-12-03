package com.erp.Dto.Request;

import java.util.List;

public record CompanyDetailsRequestDto(
        Long id,
        String name,
        Integer officeNo,
        String addressLine1,
        String addressLine2,
        String description,
        String gstNumber,
        String panNumber,
        String industryType,
        String reviewStatus,
        String reviewComment,
        String contactPersonName,
        String contactPersonEmail,
        String contactPersonPhone,
        String pincode,
        String city,
        String state,
        String companyEmail,
        List<DocumentDetailsRequestDto> documentDetails
) {}
