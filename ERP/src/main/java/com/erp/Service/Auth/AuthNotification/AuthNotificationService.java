package com.erp.Service.Auth.AuthNotification;

import com.erp.Model.GenericUser;

public interface AuthNotificationService {

    void notifyLogin(GenericUser user);

    void notifyLogout(GenericUser user);

    void notifyRefresh(GenericUser user);

    void notifyInactiveLoginAttempt(String email);

    void notifyFailedLoginAttempt(String email);
}
