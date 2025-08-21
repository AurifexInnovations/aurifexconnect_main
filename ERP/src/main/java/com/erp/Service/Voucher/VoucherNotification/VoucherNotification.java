package com.erp.Service.Voucher.VoucherNotification;

import com.erp.Model.Voucher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class VoucherNotification {

    public void notifyVoucherGenerated(Voucher voucher) {
        log.info("🧾 Voucher generated: Type={}, ID={}, Index={}, Period={} to {}",
                voucher.getVoucherType(),
                voucher.getVoucherId(),
                voucher.getVoucherIndex(),
                voucher.getStartDate(),
                voucher.getEndDate());
    }

    public void notifyVoucherFetched(Voucher voucher) {
        log.info("📄 Voucher fetched: ID={}, Type={}, Index={}",
                voucher.getVoucherId(),
                voucher.getVoucherType(),
                voucher.getVoucherIndex());
    }
}