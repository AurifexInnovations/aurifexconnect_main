package com.erp.Repository.Amc;

import com.erp.Model.Amc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AmcRepository extends JpaRepository<Amc, Long> {

    @Query("""
    SELECT a FROM Amc a
    WHERE a.amcStatus IN ('DRAFT', 'ACTIVE')
      AND a.remainCycle > 0
""")
    List<Amc> findEligibleAmcs();

    Optional<Amc> findBySalesOrderSalesOrderNumber(Long salesOrderNumber);

    List<Amc> findAllByBranchBranchId(Long branchId);



}
