package com.erp.Repository.Master;

import com.erp.Enum.VoucherType;
import com.erp.Model.Master;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MasterRepository extends JpaRepository<Master,Long> {

    List<Master> findByVoucherTypeAndMasterId(VoucherType voucherType, Long relatedId);

    @Query(value = """
    SELECT 
        strftime(?1, datetime(created_date / 1000, 'unixepoch')) AS period,
        SUM(amount) AS total_amount
    FROM master
    WHERE voucher_type = ?2
    GROUP BY period
    ORDER BY period ASC
    """, nativeQuery = true)
    List<Object[]> getPurchaseSalesSummary(String format, String voucherType);
    List<Master> findByReferenceMaster(Master invoice);
}
