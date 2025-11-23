package com.erp.Repository.Bill;

import com.erp.Model.BillItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillItemRepository extends JpaRepository<BillItem, Long> {

    List<BillItem> findByBillIdAndIsActiveTrue(Long billId);


}
