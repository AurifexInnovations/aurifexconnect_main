package com.erp.Controller.salesOrder;

import com.erp.Dto.Request.SalesOrderRequestDto;
import com.erp.Dto.Response.SalesOrderResponseDto;
import com.erp.Service.salesOrder.SalesOrderService;
import com.erp.Utility.ListResponseStructure;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sales-orders")
@RequiredArgsConstructor
@Slf4j
public class SalesOrderController {

    private final SalesOrderService salesOrderService;

    @PostMapping
    public ResponseEntity<ResponseStructure<SalesOrderResponseDto>> create(
            @RequestBody SalesOrderRequestDto dto) {

        return ResponseBuilder.success(
                HttpStatus.CREATED,
                "Sales order created",
                salesOrderService.create(dto)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseStructure<SalesOrderResponseDto>> update(
            @PathVariable Long id,
            @RequestBody SalesOrderRequestDto dto) {

        return ResponseBuilder.success(
                HttpStatus.OK,
                "Sales order updated",
                salesOrderService.update(id, dto)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseStructure<SalesOrderResponseDto>> getById(@PathVariable Long id) {
        return ResponseBuilder.success(
                HttpStatus.OK,
                "Sales order fetched",
                salesOrderService.getById(id)
        );
    }

    @GetMapping
    public ResponseEntity<ListResponseStructure<SalesOrderResponseDto>> getAll() {
        return ResponseBuilder.success(
                HttpStatus.OK,
                "Sales orders list",
                salesOrderService.getAll()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseStructure<String>> delete(@PathVariable Long id) {
        salesOrderService.delete(id);
        return ResponseBuilder.success(
                HttpStatus.OK,
                "Sales order deleted",
                "SUCCESS"
        );
    }
}
