package com.erp.Dto.Request;

import com.erp.Enum.AccountStatus;
import com.erp.Enum.Banks;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BankAccountGetRequest {

    private PaginationRequest paginationRequest;

    // Filters
    private AccountStatus accountStatus;
    private Double balanceMin;
    private Double balanceMax;
    private String createdBy;

    // Search
    private String accountNumber;
    private Banks bankName;
    private Map<String, String> searchFilters;

    // Date filter
    private DateRequest dateRequest;

    // Sorting
    private OrderByRequest orderByRequest;
}
