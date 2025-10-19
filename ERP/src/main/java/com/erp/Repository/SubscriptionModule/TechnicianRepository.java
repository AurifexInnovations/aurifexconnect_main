package com.erp.Repository.SubscriptionModule;

import com.erp.Model.TechnicianEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TechnicianRepository extends JpaRepository<TechnicianEntity, Long> {
    Optional<TechnicianEntity> findByTechnicianId(Long technicianId);
}