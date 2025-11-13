package com.erp.Repository.Lead;

import com.erp.Model.LeadProductMapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeadProductMapperRepository extends JpaRepository<LeadProductMapper, Long> {
    void deleteByLeadId(Long leadId);
}
