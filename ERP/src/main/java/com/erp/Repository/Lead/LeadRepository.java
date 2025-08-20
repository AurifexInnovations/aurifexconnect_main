package com.erp.Repository.Lead;

import com.erp.Enum.LeadStatus;
import com.erp.Model.Lead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LeadRepository extends JpaRepository<Lead, Long> {
    List<Lead> findByStatus(LeadStatus status);
    List<Lead> findByAssignedToId(Long assignedToId);
    long countByStatus(LeadStatus status);
}