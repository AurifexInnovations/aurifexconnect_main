package com.erp.Repository.Branch;

import com.erp.Enum.BranchStatus;
import com.erp.Model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {

    List<Branch> findByBranchIdOrBranchNameOrLocationOrBranchStatus(long branchId, String name,String location,BranchStatus branchStatus);

    List<Branch> findBranchByInventories_ItemName(String itemName);
}
