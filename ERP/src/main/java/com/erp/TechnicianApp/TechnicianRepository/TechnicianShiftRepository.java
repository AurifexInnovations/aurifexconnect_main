package com.erp.TechnicianApp.TechnicianRepository;

import com.erp.TechnicianApp.TechnicianModel.TechnicianShift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TechnicianShiftRepository extends JpaRepository<TechnicianShift, Long> {

    Optional<TechnicianShift> findByUser_IdAndActiveTrue(Long userId);

    List<TechnicianShift> findByUser_IdOrderByShiftStartDesc(Long userId);
}
