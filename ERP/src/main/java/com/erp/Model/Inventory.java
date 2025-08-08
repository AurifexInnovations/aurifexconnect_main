package com.erp.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
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

    @Column(name = "item_quantity", nullable = false)
    private double itemQuantity;

    @Column(name = "item_description")
    private String itemDescription;

    @Column(name = "item_cost", nullable = false)
    private double itemCost;

    @Column(name = "categories")
    private String categories;
    private double lowStockThreshold;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "last_modified_at")
    private LocalDateTime lastModifiedAt;

    @ManyToOne
    @JoinColumn(name = "branch_branch_id", referencedColumnName = "branch_id")
    private Branch branch;

    @OneToMany(mappedBy = "inventory")
    private List<LineItems> lineItems;

    @OneToMany(mappedBy = "inventory")
    private List<InventoryMovement> inventoryMovement;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Tax> taxes;
}
