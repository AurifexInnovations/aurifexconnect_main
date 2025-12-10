
package com.erp.Model;

import com.erp.Enum.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "inventoryV2")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class InventoryV2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long itemId;

    // Basic inventory info
    @Column(name = "item_name")
    private String itemName;

    @Column(name = "item_description")
    private String itemDescription;

    @Column(name = "low_stock_threshold")
    private Double lowStockThreshold;

    // Timestamps
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "last_modified_at")
    private LocalDateTime lastModifiedAt;

    // Branch relation
    @ManyToOne
    @JoinColumn(name = "branch_id", referencedColumnName = "branch_id")
    private Branch branch;

    // Product metadata
    @Column(name = "brand_name")
    private String brandName;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_categories")
    private ProductCategories productCategories;

    @Column(name = "hsn_code")
    private String hsnCode;

    @Column(name = "sku_code")
    private String skuCode;

    @Column(name = "ean")
    private String ean;

    @Column(name = "is_returnable")
    private boolean isReturnable;

    @OneToOne
    @JoinColumn(name = "tax_id")
    private Tax tax;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_status")
    private ProductStatus productStatus;

    @Column(name = "active")
    private boolean active;

    // -------------------------------
    // VARIANT FIELDS (EXACT MATCH)
    // -------------------------------

    @Column(name = "stock_quantity")
    private Integer stockQuantity;

    @Column(name = "selling_price_type")
    private String sellingPriceType;

    @Column(name = "selling_price")
    private Double sellingPrice;

    @Column(name = "purchase_price_type")
    private String purchasePriceType;

    @Column(name = "purchase_price")
    private Double purchasePrice;

    @Column(name = "unit_type")
    private String unitType;

    @Column(name = "unit_type_value")
    private Double unitTypeValue;

    @Column(name = "measurement_type")
    private String measurementType;

    @Column(name = "measurement")
    private Double measurement;

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    @ManyToMany(mappedBy = "inventories")
    private List<Service> services;

    // -------------------------------
    // RENTAL FIELDS
    // -------------------------------

    @Column(name = "is_rentable")
    private boolean rentable;

    @Column(name = "default_rental_rate")
    private Double defaultRentalRate;

    @Column(name = "rental_rate_frequency")
    @Enumerated(EnumType.STRING)
    private RentalRateFrequency rentalRateFrequency;   // Daily / Weekly / Monthly

    @Column(name = "default_deposit_amount")
    private Long defaultDepositAmount;

    @Column(name = "insurance_value")
    private Long insuranceValue;

    @Column(name = "rental_product_quantity")
    private Long rentalProductQuantity;

    @Column(name = "rental_product_status")
    @Enumerated(EnumType.STRING)
    private RentalProductStatus rentalProductStatus;  // ACTIVE / INACTIVE / DAMAGED / MAINTENANCE / AVAILABLE / RESERVED / RENTED / OUT_ON_RENT

}
