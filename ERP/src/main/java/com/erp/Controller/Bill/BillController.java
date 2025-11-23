package com.erp.Controller.Bill;

import com.erp.Dto.Request.BillRequestDTO;
import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Request.UpdateBillRequestDTO;
import com.erp.Dto.Response.BillResponseDTO;
import com.erp.Dto.Response.ResultDto;
import com.erp.Service.Bill.BillService;
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
@RequestMapping("/api/v1/bills")
public class BillController {

    @Autowired
    private BillService billService;

    // ------------------------ CREATE ------------------------
    @PostMapping("/create")
    public ResponseEntity<ResponseStructure<BillResponseDTO>> createBill(
            @RequestBody @Valid BillRequestDTO dto) {

        log.info("START :: createBill() with request: {}", dto);

        BillResponseDTO response = billService.createBill(dto);

        log.info("END :: createBill() => Bill created with ID: {}", response.getBillId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseStructure.<BillResponseDTO>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Bill created successfully")
                        .data(response)
                        .build());
    }

    // ------------------------ LIST / FILTER ------------------------
    @PostMapping("/filter")
    public ResponseEntity<ResponseStructure<ResultDto<BillResponseDTO>>> filterBills(
            @RequestBody FilterRequest filterRequest) {

        log.info("START :: filterBills() with filterRequest: {}", filterRequest);

        ResultDto<BillResponseDTO> response = billService.getFilteredBills(filterRequest);

        log.info("END :: filterBills() => totalRecords: {}", response.getCount());

        return ResponseBuilder.success(HttpStatus.OK, "Bills retrieved successfully", response);
    }

    // ------------------------ DETAIL ------------------------
    @GetMapping("/get/{id}")
    public ResponseEntity<ResponseStructure<BillResponseDTO>> getBill(@PathVariable Long id) {

        log.info("START :: getBill() for ID: {}", id);

        BillResponseDTO response = billService.getBillById(id);

        log.info("END :: getBill() => Bill Number: {}", response.getBillNumber());

        return ResponseEntity.ok(
                ResponseStructure.<BillResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("Bill details fetched successfully")
                        .data(response)
                        .build());
    }

    // ------------------------ UPDATE ------------------------
    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseStructure<BillResponseDTO>> updateBill(
            @PathVariable Long id,
            @RequestBody @Valid UpdateBillRequestDTO dto) {

        log.info("START :: updateBill() for ID: {}, request: {}", id, dto);

        BillResponseDTO response = billService.updateBill(id, dto);

        log.info("END :: updateBill() => Bill updated for ID: {}", id);

        return ResponseEntity.ok(
                ResponseStructure.<BillResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("Bill updated successfully")
                        .data(response)
                        .build());
    }

    // ------------------------ UPDATE STATUS ------------------------
    @PutMapping("/update-status/{id}")
    public ResponseEntity<ResponseStructure<String>> updateBillStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        log.info("START :: updateBillStatus() for ID: {}, status: {}", id, status);

        billService.updateBillStatus(id, status);

        log.info("END :: updateBillStatus() => Status updated for Bill ID: {}", id);

        return ResponseEntity.ok(
                ResponseStructure.<String>builder()
                        .status(HttpStatus.OK.value())
                        .message("Bill status updated successfully")
                        .data(status)
                        .build());
    }

    // ------------------------ DEACTIVATE ------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseStructure<String>> deactivateBill(@PathVariable Long id) {

        log.info("START :: deactivateBill() for ID: {}", id);

        billService.deactivateBill(id);

        log.info("END :: deactivateBill() => Bill marked inactive for ID: {}", id);

        return ResponseEntity.ok(
                ResponseStructure.<String>builder()
                        .status(HttpStatus.OK.value())
                        .message("Bill deactivated successfully")
                        .data("Bill has been marked inactive")
                        .build());
    }
}
