package com.erp.Repository.costumer;


import com.erp.Model.CustomerDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerDetailsRepository extends JpaRepository<CustomerDetails, Long> {
    List<CustomerDetails> findByBranch_BranchId(long branchId);
}
