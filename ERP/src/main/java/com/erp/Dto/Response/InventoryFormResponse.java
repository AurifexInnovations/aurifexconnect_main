package com.erp.Dto.Response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class InventoryFormResponse {
    private Long itemId;
    private String itemName;
    private String skuCode;
    private Double sellingPrice;
    private BigDecimal taxRate;
    private List<String> documentUrls;
}
