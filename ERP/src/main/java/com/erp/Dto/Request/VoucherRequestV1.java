package com.erp.Dto.Request;

import com.erp.Enum.VoucherTypeV1;
import lombok.Data;

import java.time.LocalDate;

@Data
public class VoucherRequestV1 {
        private String voucherNo;
        private VoucherTypeV1 voucherType;
        private LocalDate voucherDate;
        private String financialYear;
}