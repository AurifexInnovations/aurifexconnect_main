package com.erp.Dto.Response;

import com.erp.Dto.Request.DocumentDetailsRequestDto;

import java.util.List;

public record CompanyDetailsResponseDto(
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
        String reviewedBy,
        String companyEmail,
        List<DocumentDetailsRequestDto> documentDetails
) {
}
