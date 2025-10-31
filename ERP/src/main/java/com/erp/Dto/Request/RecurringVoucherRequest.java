package com.erp.Dto.Request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class RecurringVoucherRequest {

    @NotNull(message = "Voucher date is required")
    private LocalDate voucherDate;

    @NotBlank(message = "Narration is required")
    @Size(max = 500, message = "Narration cannot exceed 500 characters")
    private String narration;

    @NotEmpty(message = "Transaction entries are required")
    @Valid
    private List<TransactionEntryRequest> transactionEntries;

    @NotNull(message = "Recurring frequency is required")
    private String recurringFrequency; // DAILY, WEEKLY, MONTHLY, YEARLY

    @NotNull(message = "Recurring end date is required")
    private LocalDate recurringEndDate;

    private String referenceNumber;

    /**
     * For Recurring Voucher: Automatically generates vouchers based on frequency
     * Used for regular expenses, salaries, rent, etc.
     */
}
