package com.erp.Model;

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
    private long itemId;

    private String itemName;
    private double itemQuantity;
    private String itemDescription;
    private double itemCost;
    private String categories;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime lastModifiedAt;

    @OneToMany(mappedBy = "inventory")
    private List<LineItems> lineItems;

    @ManyToOne
    private Branch branch;

    @OneToMany
    private List<InventoryMovement> inventoryMovement;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Tax> taxes;
}
