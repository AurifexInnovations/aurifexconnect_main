package com.erp.Repository.TechnicianTracking;

import com.erp.Model.TechTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TechTrackRepository extends JpaRepository<TechTrack, Long> {
    boolean existsByTechnicianId(Long technicianId);
}
