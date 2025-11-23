package com.erp.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoucherDTO {
    private Integer voucherId;
    private String voucherName;
    private String voucherCode;
    private String voucherCategory;
    private String voucherAppliesTo;   // JSON as String
    private String defaultSeriesPrefix;
}