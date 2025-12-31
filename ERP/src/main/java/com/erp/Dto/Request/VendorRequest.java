package com.erp.Dto.Request;

import com.erp.Enum.VendorStatus;
import com.erp.Enum.VendorType;
import lombok.Data;

@Data
public class VendorRequest {

    private VendorType vendorType;
    private String companyName;
    private String contactPersonName;
    private String email;
    private String phone;
    private String alternatePhone;

    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private String pincode;

    private String gstNumber;
    private String panNumber;
    private String msmeNumber;
    private Boolean gstRegistered;

    private Integer paymentTermsDays;
    private String preferredCurrency;

    private String bankName;
    private String bankAccountNumber;
    private String bankIfscCode;

    private VendorStatus status;
    private Integer rating;
}
