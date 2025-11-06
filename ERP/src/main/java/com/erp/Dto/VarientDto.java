package com.erp.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VarientDto {
    private long id;
    private long itemId;
    private int stockQuantity;
    private String sellingPriceType;
    private double sellingPrice;
    private String purchasePriceType;
    private double purchasePrice;
    private String unitType;
    private double unitTypeValue;
    private String measurementType;
    private double measurement;
    private LocalDateTime expiryDate;
}
