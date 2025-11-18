package com.erp.Service.Shipment;

import com.erp.CustomRepository.ShipmentCustomRepository;
import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Request.ShipmentDetailsRequestDto;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.ShipmentDetailsResponseDTO;
import com.erp.Dto.Response.ShipmentResponseDto;
import com.erp.Enum.ShipmentStatus;
import com.erp.Exception.ResourceNotFoundException;

import com.erp.Exception.ShipmentException.ShipmentNotFoundException;
import com.erp.Mapper.shipment.ShipmentMapper;
import com.erp.Model.GenericUser;
import com.erp.Model.ShipmentDetails;
import com.erp.Repository.Shipment.ShipmentRepository;
import com.erp.Security.util.UserIdentity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class ShipmentServiceImpl implements ShipmentService {
    private final ShipmentCustomRepository shipmentCustomRepository;
    private final ShipmentRepository shipmentRepository;
    private final ShipmentMapper shipmentMapper;
    private final UserIdentity userIdentity;

    private static final String SHIPMENT_CREATION_MESSAGE = "Shipment created successfully";
    private static final String SHIPMENT_UPDATED_MESSAGE = "Shipment updated successfully";

    @Override
    public ShipmentDetailsResponseDTO createShipment(ShipmentDetailsRequestDto requestDto) {

        if (Objects.isNull(requestDto)) {
            log.error("Cannot create or update shipment — request body is null");
            throw new ResourceNotFoundException("Request cannot be null");
        }

        GenericUser user = userIdentity.getCurrentUser();

        if (Objects.nonNull(requestDto.getShipmentId())) {
            log.info("Updating shipment with ID: {}", requestDto.getShipmentId());

            ShipmentDetails existingShipment = shipmentRepository.findById(requestDto.getShipmentId())
                    .orElseGet(() -> {
                        log.warn("Shipment not found with ID: {}, creating a new one", requestDto.getShipmentId());
                        return new ShipmentDetails();
                    });

            shipmentMapper.update(existingShipment, requestDto);
            existingShipment.setUpdatedAt(LocalDateTime.now());
            existingShipment.setUpdatedBy(user.getId());

            ShipmentDetails updatedShipment = shipmentRepository.save(existingShipment);

            log.info("Shipment updated successfully with ID: {}", updatedShipment.getShipmentId());
            return shipmentMapper.shipmentDetailsResponseDTO(updatedShipment);

        } else {
            log.info("Creating new shipment with reference ID: {}", requestDto.getReferenceId());

            ShipmentDetails newShipment = shipmentMapper.map(requestDto);
            newShipment.setCreatedAt(LocalDateTime.now());
            newShipment.setUpdatedAt(LocalDateTime.now());
            newShipment.setUpdatedBy(user.getId());

            ShipmentDetails savedShipment = shipmentRepository.save(newShipment);

            log.info("Shipment created successfully with ID: {}", savedShipment.getShipmentId());
            return shipmentMapper.shipmentDetailsResponseDTO(savedShipment);
        }
    }

    @Override
    public ShipmentDetailsResponseDTO getShipmentById(Long id) {
        log.info("Fetching shipment by ID: {}", id);

        ShipmentDetails shipment = shipmentRepository.findByShipmentId(id);

        if (shipment == null) throw new ShipmentNotFoundException("Shipment Not Found With Id : " + id);

        log.info("Shipment found with ID: {}", id);

        return shipmentMapper.shipmentDetailsResponseDTO(shipment);
    }

    @Override
    public ResultDto<ShipmentDetailsResponseDTO> getAllShipmentsWithPagination() {
        log.info("Fetching paginated shipments with limit: {} and offset: {}");
        //TO-DO  need to write query when for all linked data will do when all requirement will come
        List<ShipmentDetailsResponseDTO> shipments = shipmentMapper.toList(shipmentRepository.findAll());

        ResultDto<ShipmentDetailsResponseDTO> resultDto = new ResultDto<>();
        resultDto.setResults(shipments != null ? shipments : List.of());
        resultDto.setCount(shipments != null ? shipments.size() : 0);

        log.info("Fetched {} shipments", shipments.size());
        return resultDto;
    }

    @Override
    public String deleteShipmentById(Long id) {
        log.info("Attempting to delete shipment with ID: {}", id);

        Optional<ShipmentDetails> shipmentDetails = shipmentRepository.findById(id);
        if (shipmentDetails.isEmpty()) {
            log.error("Cannot delete — Shipment not found with ID: {}", id);
            throw new ShipmentNotFoundException("Shipment not found with ID: " + id);
        }

        ShipmentDetails shipmentDetails1 = shipmentDetails.get();
        shipmentDetails1.setShipmentStatus(ShipmentStatus.CANCELLED);
        shipmentRepository.save(shipmentDetails1);
        log.info("Shipment deleted successfully with ID: {}", id);
        return "Shipment deleted successfully";
    }

    @Override
    public ResultDto<ShipmentDetailsResponseDTO> getAllShipments(FilterRequest filterRequest) {
        ResultDto<ShipmentDetailsResponseDTO> list = shipmentCustomRepository.getShipmentsPagination(filterRequest);
        return list;
    }

}
