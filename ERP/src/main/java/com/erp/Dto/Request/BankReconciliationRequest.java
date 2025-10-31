package com.erp.Dto.Request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankReconciliationRequest {

    @NotNull(message = "Bank account ID is required")
    private Long bankAccountId;

    @NotNull(message = "From date is required")
    private LocalDate fromDate;

    @NotNull(message = "To date is required")
    private LocalDate toDate;

    private List<ReconciliationMatchRequest> matches;

    private String notes;
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class ReconciliationMatchRequest {

    @NotNull(message = "Statement line ID is required")
    private Long statementLineId;

    @NotNull(message = "Transaction entry ID is required")
    private Long transactionEntryId;

    private String notes;
}
