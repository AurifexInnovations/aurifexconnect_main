package com.erp.Projection;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface SalesOrderProjection {

    Long getSalesOrderNumber();
    Long getQuotationId();
    Long getCustomerId();
    String getPhoneNumber();
    String getAlternatePhoneNumber();
    LocalDate getSalesOrderDate();
    String getCompanyName();
    String getEmail();
    String getAddress();
    String getLandmark();
    String getCity();
    String getState();
    String getCountry();
    String getPincode();
    String getLocationUrl();
    String getServiceCategory();
    BigDecimal getSqft();
    String getSalesOrderType();
    String getStatus();
    String getNotes();
    String getServiceType();
}
