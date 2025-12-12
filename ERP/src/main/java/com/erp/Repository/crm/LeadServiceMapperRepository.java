package com.erp.Repository.crm;

import com.erp.Model.LeadServiceMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeadServiceMapperRepository extends JpaRepository<LeadServiceMapper, Long> {
    List<LeadServiceMapper> findByLeadId(Long id);

    @Query("SELECT l.serviceId FROM LeadServiceMapper l WHERE l.leadId = :leadId")
    List<Long> findServiceIdsByLeadId(@Param("leadId") Long id);
}
