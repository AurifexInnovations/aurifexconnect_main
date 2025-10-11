package com.erp.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RazorpayRequest {
    private String amount; // in paise (e.g., ₹500 = "50000")
    private String currency = "INR";
    private String receipt;
    private boolean payment_capture = true;

}