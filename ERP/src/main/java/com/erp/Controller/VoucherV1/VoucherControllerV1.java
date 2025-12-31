package com.erp.Controller.VoucherV1;

import com.erp.Dto.Request.VoucherRequestV1;
import com.erp.Model.VoucherV1;
import com.erp.Service.VoucherV1.VoucherServiceV1;
import com.erp.Utility.ListResponseStructure;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vouchers")
@RequiredArgsConstructor
public class VoucherControllerV1 {

    private final VoucherServiceV1 voucherService;

    // CREATE VOUCHER
    @PostMapping
    public ResponseEntity<ResponseStructure<VoucherV1>> create(@RequestBody VoucherRequestV1 request) {
        VoucherV1 voucher = voucherService.create(request);
        return ResponseBuilder.success(HttpStatus.CREATED, "Voucher created successfully", voucher);
    }

    // UPDATE VOUCHER  --> /api/v1/vouchers?id=1
    @PutMapping
    public ResponseEntity<ResponseStructure<VoucherV1>> update(@RequestParam Long id,
                                                               @RequestBody VoucherRequestV1 request) {
        VoucherV1 voucher = voucherService.update(id, request);
        return ResponseBuilder.success(HttpStatus.OK, "Voucher updated successfully", voucher);
    }

    // DELETE VOUCHER  --> /api/v1/vouchers?id=1
    @DeleteMapping
    public ResponseEntity<ResponseStructure<String>> delete(@RequestParam Long id) {
        voucherService.delete(id);
        return ResponseBuilder.success(HttpStatus.OK, "Voucher deleted successfully", "Deleted");
    }

    // GET ALL VOUCHERS
    @GetMapping
    public ResponseEntity<ListResponseStructure<VoucherV1>> getAll() {
        List<VoucherV1> vouchers = voucherService.getAll();
        return ResponseBuilder.success(HttpStatus.OK, "Vouchers fetched successfully", vouchers);
    }

    // GET VOUCHER BY ID  --> /api/v1/vouchers/by-id?id=1
    @GetMapping("/by-id")
    public ResponseEntity<ResponseStructure<VoucherV1>> getById(@RequestParam Long id) {
        VoucherV1 voucher = voucherService.getById(id);
        return ResponseBuilder.success(HttpStatus.OK, "Voucher fetched successfully", voucher);
    }
}
