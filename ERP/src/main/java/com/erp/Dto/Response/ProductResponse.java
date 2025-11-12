package com.erp.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@AllArgsConstructor
@Data
public class ProductResponse {
    private Long itemId;
    private String itemName;
    private String brandName;
    private String categories;
    private String hsnCode;
    private Double totalStockQuantity;
    private LocalDate nearestExpiryDate;
}