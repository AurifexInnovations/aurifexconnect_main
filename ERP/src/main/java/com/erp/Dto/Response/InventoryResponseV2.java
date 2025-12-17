package com.erp.Dto.Response;

import com.erp.Enum.ProductCategories;
import com.erp.Enum.ProductStatus;
import com.erp.Enum.RentalProductStatus;
import com.erp.Enum.RentalRateFrequency;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class InventoryResponseV2 {

    private Long itemId;

    // Basic inventory info
    private String itemName;
    private String itemDescription;
    private Double lowStockThreshold;

    // Timestamp fields
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    // Branch
    private Long branchId;

    // Product metadata
    private String brandName;
    private ProductCategories productCategories;
    private String hsnCode;
    private String skuCode;
    private String ean;
    private boolean isReturnable;
    private Long taxId;
    private ProductStatus productStatus;
    private boolean active;

    // Variant data
    private Double stockQuantity;
    private String sellingPriceType;
    private Double sellingPrice;
    private String purchasePriceType;
    private Double purchasePrice;
    private String unitType;
    private Double unitTypeValue;
    private String measurementType;
    private Double measurement;
    private LocalDateTime expiryDate;

    // Rental Data
    private boolean rentable;
    private Double defaultRentalRate;
    private RentalRateFrequency rentalRateFrequency;
    private Long defaultDepositAmount;
    private Long insuranceValue;
    private Long rentalProductQuantity;
    private RentalProductStatus rentalProductStatus;
}
