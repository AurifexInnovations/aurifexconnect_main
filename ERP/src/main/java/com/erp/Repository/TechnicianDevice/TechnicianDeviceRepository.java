package com.erp.Repository.TechnicianDevice;

import com.erp.Model.TechnicianDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TechnicianDeviceRepository extends JpaRepository<TechnicianDevice, Long> {
    Optional<TechnicianDevice> findByTechnician_IdAndFcmToken(Long technicianId, String fcmToken);

    @Query("""
       select td.fcmToken
       from TechnicianDevice td
       where td.technician.id = :technicianId
    """)
    List<String> findTokensByTechnicianId(Long technicianId);
}
