package com.erp.Dto.Request;

import com.erp.Enum.ShipmentReferenceType;
import com.erp.Enum.ShipmentStatus;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentDetailsRequestDto {

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

}
