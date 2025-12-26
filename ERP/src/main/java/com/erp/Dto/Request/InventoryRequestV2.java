package com.erp.Dto.Request;

import com.erp.Dto.VarientDto;
import com.erp.Enum.*;
import com.erp.Model.File;
import com.erp.Model.FileInfoDto;
import com.erp.Model.Tax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.Base64;
import java.util.List;

@Getter
@Setter
public class InventoryRequestV2 {

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

    // -------------------------------
    //   Added List of Variant DTOs
    // -------------------------------
    private List<VarientDto> variants;


    private boolean rentable;
    private Double defaultRentalRate;
    private RentalRateFrequency rentalRateFrequency;
    private Long defaultDepositAmount;
    private Long insuranceValue;
    private Long rentalProductQuantity;
    private RentalProductStatus rentalProductStatus;

    private List<FileInfoDto> files;
}



// Create FileInfoDto With fileName, fileType, fileData
// Use This in request DTO
// Convert Image to Base64 to Actual File type