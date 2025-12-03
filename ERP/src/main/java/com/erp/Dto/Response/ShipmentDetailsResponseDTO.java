package com.erp.Dto.Response;

import com.erp.Enum.ShipmentReferenceType;
import com.erp.Enum.ShipmentStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentDetailsResponseDTO {

    private Long shipmentId;
    private ShipmentReferenceType referenceType;
    private Long referenceId;
    private Long invoiceId;
    private Boolean isBilled;
    private Long fromBranchId;
    private Long toBranchId;
    private String carrierName;
    private String trackingNumber;
    private String vehicleNumber;
    private LocalDateTime shipmentDate;
    private LocalDateTime expectedDeliveryDate;
    private LocalDateTime actualDeliveryDate;
    private ShipmentStatus shipmentStatus;
    private String remarks;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long updatedBy;
}
