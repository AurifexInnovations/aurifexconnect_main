package com.erp.Service.ServiceType.ServiceTypeNotification;

import com.erp.Model.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ServiceTypeNotifcationImpl implements ServiceTypeNotification {

    @Override
    public void notifyServiceAdded(Service service) {
        log.info("📢 Service Added: {}", service.getServiceName());
    }

    @Override
    public void notifyServiceUpdated(Service service) {
        log.info("🔄 Service Updated: {}", service.getServiceName());
    }

    @Override
    public void notifyServiceDeleted(Service service) {
        log.info("❌ Service Deleted: {}", service.getServiceName());
    }
}
