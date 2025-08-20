package com.erp.Service.Tax.TaxNotification;

import com.erp.Model.Tax;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TaxServiceNotification {

    public void notifyTaxCreated(Tax tax) {
        log.info("📢 Tax created: {}", tax.getTaxName());
    }

    public void notifyTaxUpdated(Tax tax) {
        log.info("✏️ Tax updated: {}", tax.getTaxName());
    }

    public void notifyTaxDeleted(Tax tax) {
        log.info("❌ Tax deleted: {}", tax.getTaxName());
    }
}