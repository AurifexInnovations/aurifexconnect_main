package com.erp.Repository.Lead;

import com.erp.Model.Leads;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import java.util.Optional;

@Repository
public interface LeadRepositorys extends JpaRepository<Leads, Long> {
    boolean existsByEmail(String email);

    Optional<Leads> findByEmail(String email);

    List<Leads> findByBranch_BranchId(long branchId);
}
