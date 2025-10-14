package com.erp.Dto.Request;

public record CompanyReviewRequestDto(Long companyId,
                                     String reviewStatus,
                                     String reviewComment,
                                     String reviewedBy) {
}