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
    private Double stockQuantity;
    private String sellingPriceType;
    private double sellingPrice;
    private String purchasePriceType;
    private double purchasePrice;
    private String unitType;
    private double unitTypeValue;
    private String measurementType;
    private double measurement;
    private LocalDateTime expiryDate;

    public VarientDto(long id, Double stockQuantity, String sellingPriceType, double sellingPrice, String purchasePriceType,
                      double purchasePrice, String unitType, double unitTypeValue, String measurementType,
                      double measurement, LocalDateTime expiryDate) {
        this.id = id;
        this.stockQuantity = stockQuantity;
        this.sellingPriceType = sellingPriceType;
        this.sellingPrice = sellingPrice;
        this.purchasePriceType = purchasePriceType;
        this.purchasePrice = purchasePrice;
        this.unitType = unitType;
        this.unitTypeValue = unitTypeValue;
        this.measurementType = measurementType;
        this.measurement = measurement;
        this.expiryDate = expiryDate;
    }
}
