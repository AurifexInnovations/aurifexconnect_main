package com.erp.Controller.salesOrder;

import com.erp.Dto.Request.SalesOrderRequestDto;
import com.erp.Dto.Response.SalesOrderFullResponseDto;
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

import java.util.List;

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


    @GetMapping
    public ResponseEntity<ListResponseStructure<SalesOrderFullResponseDto>> getAllSalesOrders(

            @RequestParam(required = false) Long salesOrderId,

            @RequestParam(defaultValue = "10") int limit,

            @RequestParam(defaultValue = "0") int offset
    ) {

        log.info("GET /api/sales-orders called");

        List<SalesOrderFullResponseDto> list =
                salesOrderService.getAll(salesOrderId, limit, offset);

        return ResponseBuilder.success(
                HttpStatus.OK,
                "Sales orders fetched successfully",
                list
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseStructure<String>> delete(@PathVariable Long id) {

        log.info("API request to delete Sales Order with id: {}", id);

        salesOrderService.delete(id);

        return ResponseBuilder.success(
                HttpStatus.OK,
                "Sales order deleted successfully",
                "SUCCESS"
        );
    }

}
