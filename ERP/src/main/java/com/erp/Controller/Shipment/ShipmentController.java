package com.erp.Controller.Shipment;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Request.ShipmentDetailsRequestDto;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.ShipmentDetailsResponseDTO;
import com.erp.Dto.Response.ShipmentResponseDto;
import com.erp.Model.ShipmentDetails;
import com.erp.Service.Shipment.ShipmentService;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import com.erp.Utility.SimpleErrorResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/shipment")
@RequiredArgsConstructor
public class ShipmentController
{
    private final ShipmentService shipmentService;


    @PostMapping
    public ResponseEntity<ResponseStructure<ShipmentDetailsResponseDTO>> createShipment(@RequestBody @Valid ShipmentDetailsRequestDto requestDto) {
        log.info("Controller: createShipment called for referenceId={}", requestDto.getReferenceId());
        ShipmentDetailsResponseDTO response = shipmentService.createShipment(requestDto);
        return ResponseBuilder.success(HttpStatus.CREATED, "Shipment Created", response);
    }

    @GetMapping("/byid")
    public ResponseEntity<ResponseStructure<ShipmentDetailsResponseDTO>> getShipmentById(@RequestParam Long id) {
        log.info("Controller: getShipmentById called for shipmentId={}", id);
        ShipmentDetailsResponseDTO response = shipmentService.getShipmentById(id);
        return ResponseBuilder.success(HttpStatus.OK, "Fetched Shipment Details", response);
    }


    @GetMapping
    public ResponseEntity<ResponseStructure<ResultDto<ShipmentDetailsResponseDTO>>> getAllShipments() {
        log.info("Controller: getAllShipments called with limit={} and offset={}");
        ResultDto<ShipmentDetailsResponseDTO> shipments = shipmentService.getAllShipmentsWithPagination();
        return ResponseBuilder.success(HttpStatus.OK, "Fetched All Shipments", shipments);
    }


    @DeleteMapping
    public ResponseEntity<ResponseStructure<String>> deleteShipmentById(@RequestParam Long id) {
        log.info("Controller: deleteShipmentById called for shipmentId={}", id);
        String message = shipmentService.deleteShipmentById(id);
        return ResponseBuilder.success(HttpStatus.OK, "Shipment Deleted Id : "+id, message);
    }


    @PostMapping("/pagination")
    public ResponseEntity<ResponseStructure<ResultDto<ShipmentDetailsResponseDTO>>> getShipmentPagination(@RequestBody FilterRequest filterRequest)
    {
        ResultDto<ShipmentDetailsResponseDTO> list = shipmentService.getAllShipments(filterRequest);
        return ResponseBuilder.success(HttpStatus.OK, "All Shipments Are Retrieved", list);
    }
}
