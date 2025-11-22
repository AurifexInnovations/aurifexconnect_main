package com.erp.Controller.PurchaseOrder;

import com.erp.Dto.Request.CreatePORequestDTO;
import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Request.UpdatePORequestDTO;
import com.erp.Dto.Request.UpdateStatusRequest;
import com.erp.Dto.Response.PurchaseOrderResponseDTO;
import com.erp.Dto.Response.ResultDto;
import com.erp.Service.PurchaseOrder.PurchaseOrderService;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;

import jakarta.validation.Valid;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/procurement/pos")
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderService poService;

    // CREATE PO
    @PostMapping("/create")
    public ResponseEntity<ResponseStructure<PurchaseOrderResponseDTO>> createPO(
            @RequestBody @Valid CreatePORequestDTO dto) {

        PurchaseOrderResponseDTO response = poService.createPO(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseStructure.<PurchaseOrderResponseDTO>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("PO created successfully")
                        .data(response)
                        .build());
    }

    // ------------------------ LIST (FILTER) ------------------------
    @PostMapping("/filter")
    public ResponseEntity<ResponseStructure<ResultDto<PurchaseOrderResponseDTO>>> filterPurchaseOrders(
            @RequestBody FilterRequest filterRequest
    ) {
        log.info("START :: filterPurchaseOrders() with filterRequest: {}", filterRequest);

        ResultDto<PurchaseOrderResponseDTO> response =
                poService.filterPurchaseOrders(filterRequest);

        log.info("END :: filterPurchaseOrders() => totalRecords: {}", response.getCount());

        return ResponseBuilder.success(
                HttpStatus.OK,
                "Purchase Orders retrieved successfully!",
                response
        );
    }

    // PO DETAIL
    @GetMapping("/get/{id}")
    public ResponseEntity<ResponseStructure<PurchaseOrderResponseDTO>> getPODetail(@PathVariable Long id) {

        PurchaseOrderResponseDTO response = poService.getPODetail(id);

        return ResponseBuilder.success(HttpStatus.OK, "PO details fetched", response);
    }

    // UPDATE STATUS
    @PutMapping("/status/{id}")
    public ResponseEntity<ResponseStructure<PurchaseOrderResponseDTO>> updateStatus(
            @PathVariable Long id,
            @RequestBody UpdateStatusRequest request) {

        PurchaseOrderResponseDTO response = poService.updateStatus(id, request.getStatus());

        return ResponseBuilder.success(HttpStatus.OK, "PO status updated", response);
    }


    // ------------------------ UPDATE PO DETAILS ------------------------
    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseStructure<PurchaseOrderResponseDTO>> updatePO(
            @PathVariable Long id,
            @RequestBody @Valid UpdatePORequestDTO dto) {

        log.info("START :: [PurchaseOrderController] [updatePO] :: poId = {}", id);

            PurchaseOrderResponseDTO response = poService.updatePO(id, dto);

            log.info("END :: [PurchaseOrderController] [updatePO] :: PO updated successfully :: poId = {}", id);

            return ResponseBuilder.success(
                    HttpStatus.OK,
                    "PO updated successfully",
                    response
            );
    }
}
