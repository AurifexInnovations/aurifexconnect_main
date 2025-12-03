package com.erp.Repository.Shipment;

import com.erp.Model.ShipmentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentRepository extends JpaRepository<ShipmentDetails, Long> {
    ShipmentDetails findByShipmentId(Long shipmentId);
}
