package com.erp.Service.Master.MasterNotification;

import com.erp.Model.Master;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MasterServiceNotificationImpl implements MasterServiceNotification {

    @Override
    public void notifyMasterCreated(Master master) {
        log.info("✅ Master Created: ID = {}, VoucherType = {}, Amount = {}",
                master.getMasterId(), master.getVoucherType(), master.getAmount());
        // You can extend this: send email, event, webhook etc.
    }

    @Override
    public void notifyMasterUpdated(Master master) {
        log.info("✏️ Master Updated: ID = {}, VoucherType = {}, Amount = {}",
                master.getMasterId(), master.getVoucherType(), master.getAmount());
    }

    @Override
    public void notifyMasterDeleted(Master master) {
        log.info("🗑️ Master Deleted: ID = {}, VoucherType = {}, Amount = {}",
                master.getMasterId(), master.getVoucherType(), master.getAmount());
    }
}
