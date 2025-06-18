package com.erp.Model;

import com.erp.Enum.VoucherType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class LineItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long lineItemId;

    private String itemName;

    private double unitPrice;

    private double quantity;

    private double baseAmount;

    private double totalPrice;

    @Enumerated(EnumType.STRING)
    private VoucherType voucherType;

    @CreatedDate
    private LocalDate date;

    @ManyToOne
    private Inventory inventory;

    @ManyToOne
    private Master master;

    @OneToMany(mappedBy = "lineItems")
    private List<LineItemTax> lineItemTaxes;

}
