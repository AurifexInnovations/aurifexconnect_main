
package com.erp.Model;

import com.erp.Enum.ProductCategories;
import com.erp.Enum.ProductStatus;
import com.erp.Enum.TaxName;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private long itemId;

    @Column(name = "item_name")
    private String itemName;


    @Column(name="brand_name")
    private String brandName;


    @Column(name = "categories")
    private String categories;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_categories")
    private ProductCategories productCategories;

    @Column(name="hsn_code")
    private String hsnCode;

    @Column(name="sku_code")
    private String skuCode;

    @Column(name="ean")
    private String ean;

    @Column(name="is_returnable")
    private boolean isReturnable;

    @Column(name = "tax_Id")
    private long taxId;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_status")
    private ProductStatus productStatus;

    @Column(name = "branch_id")
    private long branchId;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "last_modified_at")
    private LocalDateTime lastModifiedAt;


    @Column(name = "item_quantity")
    private double itemQuantity;

    @Column(name = "item_description")
    private String itemDescription;

    @Column(name = "item_cost")
    private double itemCost;


    @Column(name = "low_stock_threshold")
    private double lowStockThreshold;
    @ManyToOne
    @JoinColumn(name = "branch_branch_id", referencedColumnName = "branch_id")
    private Branch branch;

//
//    @OneToMany(mappedBy = "inventory")
//    private List<LineItems> lineItems;

//    @OneToMany(mappedBy = "inventory")
//    private List<InventoryMovement> inventoryMovement;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Tax> taxes;

    @Column(name = "active")
    private boolean active;

}
