package com.erp.Model;

import com.erp.Enum.ShipmentReferenceType;
import com.erp.Enum.ShipmentStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "shipment_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentDetails
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipment_id")
    private Long shipmentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "reference_type")
    private ShipmentReferenceType referenceType;

    @Column(name = "reference_id")
    private Long referenceId;

    @Column(name = "invoice_id")
    private Long invoiceId;

    @Column(name = "is_billed", nullable = false)
    private Boolean isBilled = false;

    @Column(name = "from_branch_id")
    private Long fromBranchId;

    @Column(name = "to_branch_id")
    private Long toBranchId;

    @Column(name = "carrier_name")
    private String carrierName;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @Column(name = "vehicle_number")
    private String vehicleNumber;

    @Column(name = "shipment_date")
    private LocalDateTime shipmentDate;

    @Column(name = "expected_delivery_date")
    private LocalDateTime expectedDeliveryDate;

    @Column(name = "actual_delivery_date")
    private LocalDateTime actualDeliveryDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "shipment_status")
    private ShipmentStatus shipmentStatus;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private Long updatedBy;


}
