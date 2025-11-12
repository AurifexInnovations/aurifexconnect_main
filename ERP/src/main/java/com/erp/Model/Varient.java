package com.erp.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table( name = "varients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Varient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "item_id")
    private long itemId;
    @Column(name = "stock_quantity")
    private int stockQuantity;
    @Column(name = "selling_price_type")
    private String sellingPriceType;
    @Column(name = "selling_price")
    private double sellingPrice;
    @Column(name = "purchase_price_type")
    private String purchasePriceType;
    @Column(name = "purchase_price")
    private double purchasePrice;
    @Column(name = "unit_type")
    private String unitType;
    @Column(name = "unit_type_value")
    private double unitTypeValue;
    @Column(name = "measurement_type")
    private String measurementType;
    @Column(name = "measurement")
    private double measurement;
    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;
}
