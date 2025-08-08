package com.erp.Dto.Request;

import com.erp.Enum.TaxName;
import com.erp.Enum.TaxType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class InventoryRequest {

    @Pattern(regexp = "^.{2,}$",message = "Please Enter the More then 2 Character")
    @NotBlank(message="Please Enter Value")
    private String itemName;
    @DecimalMin(value = "1.0", inclusive = true, message = "Quantity must be at least 1")
    private double itemQuantity;
    private String itemDescription;
    private double itemCost;
    private String categories;
    private long branchAndInventoryId;
    private List<TaxName> applicableTaxNames;
    private double lowStockThreshold;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate expiryDate;

}