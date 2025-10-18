package com.erp.Projection;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class VoucherProjection {
    private Long voucherId;
    private String voucherType;
    private String voucherIndex;
    private LocalDate startDate;
    private LocalDate endDate;
}
