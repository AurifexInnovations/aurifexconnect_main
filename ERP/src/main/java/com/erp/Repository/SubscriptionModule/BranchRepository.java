package com.erp.Repository.SubscriptionModule;

import com.erp.Model.BranchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BranchRepository extends JpaRepository<BranchEntity, Long> {
    Optional<BranchEntity> findByBranchCode(String branchCode);
}