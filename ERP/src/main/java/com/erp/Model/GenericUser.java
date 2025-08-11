package com.erp.Model;

import org.springframework.security.core.userdetails.UserDetails;

public interface GenericUser extends UserDetails {
    long getId();
    String getEmail();
    boolean isActive();
    String getSchemaName();
}
