package com.erp.Repository.StockTransfer;

import com.erp.Model.StockTransfer;
import com.erp.Enum.StockTransferStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockTransferRepository extends JpaRepository<StockTransfer, Long> {

    // Fetch transfers by status (e.g., pending approvals)
    List<StockTransfer> findByStatus(StockTransferStatus status);

    // Fetch transfers by fromBranch id
    List<StockTransfer> findByFromBranch_BranchId(long branchId);

    // Fetch transfers by toBranch id
    List<StockTransfer> findByToBranch_BranchId(long branchId);
}
