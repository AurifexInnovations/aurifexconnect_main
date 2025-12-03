package com.erp.Mapper.shipment;

import com.erp.Dto.Request.ShipmentDetailsRequestDto;

import com.erp.Dto.Response.ShipmentDetailsResponseDTO;
import com.erp.Model.ShipmentDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class ShipmentMapper {


    public ShipmentDetails map(ShipmentDetailsRequestDto requestDto) {
        return update(null, requestDto);
    }

    public ShipmentDetails update(ShipmentDetails entity, ShipmentDetailsRequestDto requestDto) {
        if (Objects.isNull(entity)) {
            entity = new ShipmentDetails();
        }

        if (Objects.nonNull(requestDto)) {
            entity.setReferenceType(requestDto.getReferenceType());
            entity.setReferenceId(requestDto.getReferenceId());
            entity.setInvoiceId(requestDto.getInvoiceId());
            entity.setIsBilled(requestDto.getIsBilled());
            entity.setFromBranchId(requestDto.getFromBranchId());
            entity.setToBranchId(requestDto.getToBranchId());
            entity.setCarrierName(requestDto.getCarrierName());
            entity.setTrackingNumber(requestDto.getTrackingNumber());
            entity.setVehicleNumber(requestDto.getVehicleNumber());
            entity.setShipmentDate(requestDto.getShipmentDate());
            entity.setExpectedDeliveryDate(requestDto.getExpectedDeliveryDate());
            entity.setActualDeliveryDate(requestDto.getActualDeliveryDate());
            entity.setShipmentStatus(requestDto.getShipmentStatus());
            entity.setRemarks(requestDto.getRemarks());
        }

        return entity;
    }


    public ShipmentDetailsRequestDto map(ShipmentDetails entity) {
        return update(null, entity);
    }

    public ShipmentDetailsRequestDto update(ShipmentDetailsRequestDto responseDto, ShipmentDetails entity) {
        if (Objects.isNull(responseDto)) {
            responseDto = new ShipmentDetailsRequestDto();
        }

        if (Objects.nonNull(entity)) {
            responseDto.setShipmentId(entity.getShipmentId());
            responseDto.setReferenceType(entity.getReferenceType());
            responseDto.setReferenceId(entity.getReferenceId());
            responseDto.setInvoiceId(entity.getInvoiceId());
            responseDto.setIsBilled(entity.getIsBilled());
            responseDto.setFromBranchId(entity.getFromBranchId());
            responseDto.setToBranchId(entity.getToBranchId());
            responseDto.setCarrierName(entity.getCarrierName());
            responseDto.setTrackingNumber(entity.getTrackingNumber());
            responseDto.setVehicleNumber(entity.getVehicleNumber());
            responseDto.setShipmentDate(entity.getShipmentDate());
            responseDto.setExpectedDeliveryDate(entity.getExpectedDeliveryDate());
            responseDto.setActualDeliveryDate(entity.getActualDeliveryDate());
            responseDto.setShipmentStatus(entity.getShipmentStatus());
            responseDto.setRemarks(entity.getRemarks());

        }

        return responseDto;
    }


    public List<ShipmentDetailsResponseDTO> toList(List<ShipmentDetails> list)
    {
        List<ShipmentDetailsResponseDTO> res = new ArrayList<>();

        for(ShipmentDetails s : list){

            ShipmentDetailsResponseDTO dto = ShipmentDetailsResponseDTO.builder()
                    .shipmentId(s.getShipmentId())
                    .referenceType(s.getReferenceType())
                    .referenceId(s.getReferenceId())
                    .invoiceId(s.getInvoiceId())
                    .isBilled(s.getIsBilled())
                    .fromBranchId(s.getFromBranchId())
                    .toBranchId(s.getToBranchId())
                    .carrierName(s.getCarrierName())
                    .trackingNumber(s.getTrackingNumber())
                    .vehicleNumber(s.getVehicleNumber())
                    .shipmentDate(s.getShipmentDate())
                    .expectedDeliveryDate(s.getExpectedDeliveryDate())
                    .actualDeliveryDate(s.getActualDeliveryDate())
                    .shipmentStatus(s.getShipmentStatus())
                    .remarks(s.getRemarks())
                    .createdAt(s.getCreatedAt())
                    .updatedAt(s.getUpdatedAt())
                    .updatedBy(s.getUpdatedBy())
                    .build();

            res.add(dto);
        }

        return res;
    }


    public ShipmentDetailsResponseDTO shipmentDetailsResponseDTO(ShipmentDetails s){
        ShipmentDetailsResponseDTO dto = ShipmentDetailsResponseDTO.builder()
                .shipmentId(s.getShipmentId())
                .referenceType(s.getReferenceType())
                .referenceId(s.getReferenceId())
                .invoiceId(s.getInvoiceId())
                .isBilled(s.getIsBilled())
                .fromBranchId(s.getFromBranchId())
                .toBranchId(s.getToBranchId())
                .carrierName(s.getCarrierName())
                .trackingNumber(s.getTrackingNumber())
                .vehicleNumber(s.getVehicleNumber())
                .shipmentDate(s.getShipmentDate())
                .expectedDeliveryDate(s.getExpectedDeliveryDate())
                .actualDeliveryDate(s.getActualDeliveryDate())
                .shipmentStatus(s.getShipmentStatus())
                .remarks(s.getRemarks())
                .createdAt(s.getCreatedAt())
                .updatedAt(s.getUpdatedAt())
                .updatedBy(s.getUpdatedBy())
                .build();

        return dto;
    }
}
