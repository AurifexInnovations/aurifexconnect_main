package com.erp.Projection;

import com.erp.Dto.VarientDto;
import com.erp.Enum.ProductCategories;
import com.erp.Enum.ProductStatus;
import com.erp.Enum.TaxName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryAndBranchProjection {
    private long itemId;
    private String itemName;
    private double itemQuantity;
    private String itemDescription;
    private double itemCost;
    private String categories;
    private double lowStockThreshold;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    private String brandName;
    private ProductCategories productCategories;
    private String hsnCode;
    private String skuCode;
    private String ean;
    private boolean isReturnable;
    private long taxId;
    private ProductStatus productStatus;
    private long branchId;
    private String branchName;
    private String fileUrl;


    private int totalStockQuantity;
    private LocalDateTime latestExpiryDate;
    private TaxName taxName;

    private List<VarientDto> varientDtoList;

}