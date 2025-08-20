package com.erp.Service.LedgerService.LedgerServiceNotification;

import com.erp.Model.Ledger;

public interface LedgerServiceNotification {

    void notifyLedgerCreated(Ledger ledger);

    void notifyLedgerUpdated(Ledger ledger);

    void notifyLedgerDeleted(Ledger ledger);
}
