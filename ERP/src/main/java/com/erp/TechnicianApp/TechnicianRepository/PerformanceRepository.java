package com.erp.TechnicianApp.TechnicianRepository;

import com.erp.TechnicianApp.TechnicianModel.TechnicianPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PerformanceRepository extends JpaRepository<TechnicianPerformance, Long> {

 
    Optional<TechnicianPerformance> findByUser_IdAndMonthAndYear(Long userId, Integer month, Integer year);

    List<TechnicianPerformance> findByUser_Id(Long userId);
}
