package com.erp.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PurchaseSalesResponse {
    //new
    private String period;
    private double totalAmount;

}
