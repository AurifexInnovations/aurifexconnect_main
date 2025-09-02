package com.erp.TechnicianApp.TechnicianRepository;

import com.erp.TechnicianApp.TechnicianModel.CustomerSupport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerSupportRepository extends JpaRepository<CustomerSupport, Long> {
    List<CustomerSupport> findByUser_Id(Long technicianId);
    List<CustomerSupport> findByStatus(CustomerSupport.Status status);
    List<CustomerSupport> findByPriority(CustomerSupport.Priority priority);
}
