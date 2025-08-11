package com.erp.Model;

import com.erp.Enum.ReferenceType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AgainstRefMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "against_id")
    private long againstId;

    @Enumerated(EnumType.STRING)
    @Column(name = "reference_type")
    private ReferenceType referenceType;

    @Column(name = "amount")
    private double amount;

    @ManyToOne
    private Ledger ledger;

    @ManyToOne
    private Master master;
}
