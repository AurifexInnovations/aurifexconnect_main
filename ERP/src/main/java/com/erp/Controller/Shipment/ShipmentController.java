package com.erp.Controller.Shipment;

import com.erp.Dto.Request.ShipmentDetailsRequestDto;
import com.erp.Dto.Response.ShipmentResponseDto;
import com.erp.Model.ShipmentDetails;
import com.erp.Service.Shipment.ShipmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/shipment")
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentService shipmentService;


    @PostMapping
    public ResponseEntity<ShipmentResponseDto> createShipment(@RequestBody @Valid ShipmentDetailsRequestDto requestDto) {
        log.info("Controller: createShipment called for referenceId={}", requestDto.getReferenceId());
        ShipmentResponseDto response = shipmentService.createShipment(requestDto);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ShipmentResponseDto> getShipmentById(@PathVariable Long id) {
        log.info("Controller: getShipmentById called for shipmentId={}", id);
        ShipmentResponseDto response = shipmentService.getShipmentById(id);
        return ResponseEntity.ok(response);
    }


    @GetMapping
    public ResponseEntity<List<ShipmentDetails>> getAllShipments(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        log.info("Controller: getAllShipments called with limit={} and offset={}", limit, offset);
        List<ShipmentDetails> shipments = shipmentService.getAllShipmentsWithPagination(limit, offset);
        return ResponseEntity.ok(shipments);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteShipmentById(@PathVariable Long id) {
        log.info("Controller: deleteShipmentById called for shipmentId={}", id);
        String message = shipmentService.deleteShipmentById(id);
        return ResponseEntity.ok(message);
    }
}
