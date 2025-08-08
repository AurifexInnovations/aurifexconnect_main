package com.erp.Service.Master.MasterNotification;



import com.erp.Model.Master;

public interface MasterServiceNotification {

    void notifyMasterCreated(Master master);

    void notifyMasterUpdated(Master master);

    void notifyMasterDeleted(Master master);
}
