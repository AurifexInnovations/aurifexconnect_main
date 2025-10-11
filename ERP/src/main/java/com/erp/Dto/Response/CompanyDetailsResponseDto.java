package com.erp.Dto.Response;

import com.erp.Dto.Request.DocumentDetailsRequestDto;

import java.util.List;

public record CompanyDetailsResponseDto(Long id,
                                        String name,
                                        String address,
                                        String description,
                                        String gstNumber,
                                        String panNumber,
                                        String industryType,
                                        List<DocumentDetailsRequestDto> documentDetails,
                                        String reviewStatus,
                                        String reviewComment,
                                        String reviewedBy) {
}
