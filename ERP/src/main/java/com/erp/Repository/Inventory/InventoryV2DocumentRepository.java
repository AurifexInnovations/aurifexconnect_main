package com.erp.Repository.Inventory;

import com.erp.Model.InventoryV2Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryV2DocumentRepository
        extends JpaRepository<InventoryV2Document, Long> {

    List<InventoryV2Document> findByInventoryV2_ItemId(Long itemId);

    @Query("""
    select d.documentUrl
    from InventoryV2Document d
    where d.inventoryV2.itemId = :itemId
""")
    List<String> findDocumentUrlsByItemId(Long itemId);

    void deleteByInventoryV2_ItemId(Long itemId);

    boolean existsByInventoryV2_ItemId(Long itemId);
}
