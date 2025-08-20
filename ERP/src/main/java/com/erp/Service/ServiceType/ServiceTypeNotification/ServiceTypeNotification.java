package com.erp.Service.ServiceType.ServiceTypeNotification;

import com.erp.Model.Service;

public interface ServiceTypeNotification {

    void notifyServiceAdded(Service service);

    void notifyServiceUpdated(Service service);

    void notifyServiceDeleted(Service service);
}
