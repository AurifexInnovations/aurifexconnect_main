package com.erp.Repository.Lead;

import com.erp.Model.LeadProductMapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeadProductMapperRepository extends JpaRepository<LeadProductMapper, Long> {
    void deleteByLeadId(Long leadId);

    List<LeadProductMapper> findByLeadId(Long leadId);
}
