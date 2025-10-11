package com.erp.Dto.Request;

import java.util.List;

public record CompanyDetailsRequestDto(Long id,
                                       String name,
                                       String address,
                                       String description,
                                       String gstNumber,
                                       String panNumber,
                                       String industryType,
                                       String reviewStatus,
                                       String reviewComment,
                                       String reviewedBy,
                                       List<DocumentDetailsRequestDto> documentDetails) {
}
