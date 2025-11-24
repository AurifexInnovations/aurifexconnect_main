package com.erp.Controller.SalesOrder;

import com.erp.Dto.Request.CreateSORequestDTO;
import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Request.UpdateSORequestDTO;
import com.erp.Dto.Request.UpdateSalesOrderStatusRequest;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.SalesOrderResponseDTO;
import com.erp.Service.SalesOrder.SalesOrderService;
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
@RequestMapping("/api/v1/sales/orders")
public class SalesOrderController {

    @Autowired
    private SalesOrderService soService;

    // CREATE SO
    @PostMapping("/create")
    public ResponseEntity<ResponseStructure<SalesOrderResponseDTO>> createSO(
            @RequestBody @Valid CreateSORequestDTO dto) {

        SalesOrderResponseDTO response = soService.createSO(dto);

        return ResponseBuilder.success(HttpStatus.CREATED,
                "Sales Order created successfully", response);
    }

    // LIST / FILTER
    @PostMapping("/filter")
    public ResponseEntity<ResponseStructure<ResultDto<SalesOrderResponseDTO>>> filterSalesOrders(
            @RequestBody FilterRequest filterRequest) {

        ResultDto<SalesOrderResponseDTO> response = soService.filterSalesOrders(filterRequest);

        return ResponseBuilder.success(HttpStatus.OK,
                "Sales Orders retrieved successfully!", response);
    }

    // DETAIL
    @GetMapping("/get/{id}")
    public ResponseEntity<ResponseStructure<SalesOrderResponseDTO>> getSODetail(@PathVariable Long id) {

        SalesOrderResponseDTO response = soService.getSODetail(id);

        return ResponseBuilder.success(HttpStatus.OK,
                "SO details fetched", response);
    }

    // UPDATE STATUS
    @PutMapping("/status/{id}")
    public ResponseEntity<ResponseStructure<SalesOrderResponseDTO>> updateStatus(
            @PathVariable Long id,
            @RequestBody UpdateSalesOrderStatusRequest request) {

        SalesOrderResponseDTO response = soService.updateStatus(id, request.getStatus());

        return ResponseBuilder.success(HttpStatus.OK,
                "SO status updated", response);
    }

    // UPDATE SO
    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseStructure<SalesOrderResponseDTO>> updateSO(
            @PathVariable Long id,
            @RequestBody @Valid UpdateSORequestDTO dto) {

        SalesOrderResponseDTO response = soService.updateSO(id, dto);

        return ResponseBuilder.success(HttpStatus.OK,
                "SO updated successfully", response);
    }

    @DeleteMapping("/deactivate/{id}")
    public ResponseEntity<ResponseStructure<String>> deactivateSO(@PathVariable Long id) {

        log.info("[DEACTIVATE SO] id={}", id);

        soService.deactivateSO(id);

        return ResponseBuilder.success(HttpStatus.OK,
                "Sales Order deactivated successfully", "SO id " + id + " deactivated");
    }

}
