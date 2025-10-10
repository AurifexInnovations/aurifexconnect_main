package com.erp.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
class OtpData {
    private String verficationId;
    private String mobileNumber;
    private String responseCode;
    private String errorMessage;
    private String verificationStatus;
    private String authToken;
    private String transactionId;
}