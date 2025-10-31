package com.erp.Dto.Response;

import com.erp.Enum.VoucherStatus;
import com.erp.Enum.VoucherType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoucherResponse {

    private Long voucherId;
    private VoucherType voucherType;
    private String voucherIndex;
    private LocalDate voucherDate;
    private VoucherStatus voucherStatus;
    private BigDecimal totalAmount;
    private BigDecimal debitTotal;
    private BigDecimal creditTotal;
    private String narration;
    private String referenceNumber;
    private Boolean isRecurring;
    private String recurringFrequency;
    private LocalDate recurringEndDate;
    private LocalDate nextRecurringDate;
    private Boolean isReversed;
    private Long reversedVoucherId;
    private LocalDateTime createdDate;
    private String createdBy;
    private String modifiedBy;
    private LocalDateTime modifiedDate;
    private List<TransactionEntryResponse> transactionEntries;

    public VoucherResponse(Long voucherId, VoucherType voucherType, String voucherIndex, 
                          LocalDate voucherDate, VoucherStatus voucherStatus, 
                          BigDecimal totalAmount, String narration) {
        this.voucherId = voucherId;
        this.voucherType = voucherType;
        this.voucherIndex = voucherIndex;
        this.voucherDate = voucherDate;
        this.voucherStatus = voucherStatus;
        this.totalAmount = totalAmount;
        this.narration = narration;
    }
}