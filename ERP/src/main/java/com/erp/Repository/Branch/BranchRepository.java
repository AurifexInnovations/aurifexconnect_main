package com.erp.Repository.Branch;

import com.erp.Enum.BranchStatus;
import com.erp.Model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long>
{
    List<Branch> findByBranchIdOrBranchNameOrLocationOrBranchStatus(long branchId, String name,String location,BranchStatus branchStatus);

    @Query("SELECT b FROM Branch b WHERE " +
            "(:id IS NULL OR b.id = :id) AND " +
            "(:name IS NULL OR b.branchName LIKE %:name%) AND " +
            "(:location IS NULL OR b.location LIKE %:location%) AND " +
            "(:status IS NULL OR b.branchStatus = :status)")
    List<Branch> findBranches(@Param("id") Long id,
                              @Param("name") String name,
                              @Param("location") String location,
                              @Param("status") BranchStatus status);


    List<Branch> findBranchByInventories_ItemName(String itemName);

    long countByBranchStatus(BranchStatus status);

    boolean existsById(long branchId);
}
