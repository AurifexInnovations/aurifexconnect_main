package com.erp.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "inventoryv2_documents")
@Getter
@Setter
public class InventoryV2Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "document_url", nullable = false)
    private String documentUrl;

    @Column(name = "document_name")
    private String documentName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventoryv2_id", referencedColumnName = "item_id", nullable = false)
    private InventoryV2 inventoryV2;
}
