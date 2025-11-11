package com.erp.Repository.Lead;

import com.erp.Model.Leads;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeadRepositorys extends JpaRepository<Leads, Long> {
}
