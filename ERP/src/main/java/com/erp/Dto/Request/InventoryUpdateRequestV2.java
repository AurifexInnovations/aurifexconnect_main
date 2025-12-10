package com.erp.Dto.Request;

import com.erp.Dto.VarientDto;
import com.erp.Enum.ProductCategories;
import com.erp.Enum.ProductStatus;
import com.erp.Enum.RentalProductStatus;
import com.erp.Enum.RentalRateFrequency;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class InventoryUpdateRequestV2 {
    private long itemId;

    // Basic inventory info
    @Pattern(regexp = "^.{2,}$", message = "Please enter more than 2 characters")
    @NotBlank(message = "Item name is required")
    private String itemName;

    private String itemDescription;

    @DecimalMin(value = "0.0", inclusive = true, message = "Low stock threshold cannot be negative")
    private Double lowStockThreshold;

    // Branch reference
    private Long branchId;

    private String brandName;

    private ProductCategories productCategories;

    private String hsnCode;

    private String skuCode;

    private String ean;

    private boolean returnable;

    private Long taxId;

    private ProductStatus productStatus;

    private boolean active;

    // Varient Data
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

    // Rentable Data
    private boolean rentable;
    private Double defaultRentalRate;
    private RentalRateFrequency rentalRateFrequency;
    private Long defaultDepositAmount;
    private Long insuranceValue;
    private Long rentalProductQuantity;
    private RentalProductStatus rentalProductStatus;
}
