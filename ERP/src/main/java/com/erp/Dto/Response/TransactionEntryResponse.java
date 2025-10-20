package com.erp.Dto.Response;

import com.erp.Enum.EntryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEntryResponse {

    private Long entryId;
    private Long voucherId;
    private Long ledgerId;
    private String ledgerName;
    private String ledgerCode;
    private EntryType entryType;
    private BigDecimal amount;
    private String description;
    private String referenceNumber;
    private String chequeNumber;
    private LocalDateTime chequeDate;
    private Boolean isReconciled;
    private LocalDateTime createdDate;
    private String createdBy;

    public TransactionEntryResponse(Long entryId, Long ledgerId, String ledgerName, 
                                  EntryType entryType, BigDecimal amount, String description) {
        this.entryId = entryId;
        this.ledgerId = ledgerId;
        this.ledgerName = ledgerName;
        this.entryType = entryType;
        this.amount = amount;
        this.description = description;
    }
}
