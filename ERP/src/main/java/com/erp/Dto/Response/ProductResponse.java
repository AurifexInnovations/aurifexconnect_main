package com.erp.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ProductResponse {
    private Long itemId;
    private String itemName;
    private String brandName;
    private String categories;
    private String hsnCode;
    private int totalStockQuantity;
    private LocalDate nearestExpiryDate;

}