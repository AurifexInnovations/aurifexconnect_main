package com.erp.Repository.Staff;

import com.erp.Enum.Designation;
import com.erp.Enum.StaffStatus;
import com.erp.Model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    // Corrected flexible search method
    List<Staff> findByIdOrStaffNameOrDesignationOrStaffStatus(long id, String staffName, Designation designation, StaffStatus staffStatus);

    // To fetch staff by Branch ID
    List<Staff> findByBranch_BranchId(long branchId);
}
