package com.erp.Model;

import com.erp.Enum.VoucherType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "line_items")
@Getter
@Setter
public class LineItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "line_item_id")
    private long lineItemId;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "unit_price")
    private double unitPrice;

    @Column(name = "quantity")
    private double quantity;

    @Column(name = "base_amount")
    private double baseAmount;

    @Column(name = "total_price")
    private double totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "voucher_type")
    private VoucherType voucherType;

    @CreatedDate
    @Column(name = "date")
    private LocalDate date;

    @ManyToOne
    private Inventory inventory;

    @ManyToOne
    private Master master;

    @OneToMany(mappedBy = "lineItems")
    private List<LineItemTax> lineItemTaxes;
}
