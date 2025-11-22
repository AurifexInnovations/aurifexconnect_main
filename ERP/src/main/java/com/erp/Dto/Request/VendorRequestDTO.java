package com.erp.Dto.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class VendorRequestDTO {

    @NotBlank
    private String vendorName;

    private String contactPerson;

    @NotBlank
    private String phoneNumber;

    @Email
    private String emailAddress;

    @NotBlank
    private String billingAddress;

    @NotBlank
    private String paymentTerms;

    private BigDecimal creditLimit;
}
