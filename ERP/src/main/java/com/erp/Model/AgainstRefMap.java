package com.erp.Model;

import com.erp.Enum.ReferenceType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "against_ref_map")
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
    @JoinColumn(name = "ledger_ledger_id")
    private Ledger ledger;

    @ManyToOne
    @JoinColumn(name = "master_master_id")
    private Master master;
}
