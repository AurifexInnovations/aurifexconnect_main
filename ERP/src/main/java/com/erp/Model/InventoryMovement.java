package com.erp.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class InventoryMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tran_id")
    private long tranId;

    @ManyToOne
    private Voucher voucher;

    @ManyToOne
    private LineItems lineItem;

    @ManyToOne
    private Branch branch;

    @ManyToOne
    private Inventory inventory;

    @ManyToOne
    private Ledger ledger;

    @Column(name = "item_quantity")
    private double itemQuantity;

    @CreatedDate
    @Column(name = "tran_date")
    private LocalDateTime tranDate;
}