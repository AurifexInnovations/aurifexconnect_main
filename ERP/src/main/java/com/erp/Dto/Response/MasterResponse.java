package com.erp.Dto.Response;

import com.erp.Enum.ReferenceType;
import com.erp.Enum.TaxName;
import com.erp.Enum.TransactionStatus;
import com.erp.Enum.VoucherType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class MasterResponse {

    private long masterId;

    private String name;

    private VoucherType voucherType;

    private double amount;

    private ReferenceType referenceType;

    private TransactionStatus transactionStatus;

    private String description;

    private String voucherIndex;

    private LocalDateTime createdDate;

    private String createdBy;

    private String modifiedBy;

    private LocalDateTime modifiedDate;

    private List<TaxName> applicableTaxNames;

    private Map<TaxName, Double> taxBreakdown;

    private double taxTotal;

    private double totalAmount;






}
