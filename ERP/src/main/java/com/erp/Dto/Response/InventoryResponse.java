package com.erp.Dto.Response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class InventoryResponse {

    private long itemId;

    private String itemName;
    private double itemQuantity;
    private String itemDescription;
    private double itemCost;
    private String categories;
    private double lowStockThreshold;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate expiryDate;

    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

}
