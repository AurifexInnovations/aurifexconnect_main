package com.erp.Controller.AccountDashboardView;

import com.erp.Dto.VoucherDTO;
import com.erp.Service.AccountDashBoard.IVoucherService;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/vouchers")
@Tag(name = "Voucher Controller", description = "APIs for Vouchers")
@AllArgsConstructor
public class VoucherController {

    @Autowired
    IVoucherService voucherService;

    @GetMapping("/fetch-by-id/{voucherId}")
    @Operation(description = "Fetch Voucher by ID",
            responses = {@ApiResponse(responseCode = "200", description = "Voucher retrieved")})
    public ResponseEntity<ResponseStructure<VoucherDTO>> fetchById(@PathVariable Integer voucherId) {
        VoucherDTO response = voucherService.getById(voucherId);
        return ResponseBuilder.success(HttpStatus.OK, "Voucher retrieved successfully", response);
    }

    @PostMapping("/create-voucher")
    @Operation(description = "Create Voucher",
            responses = {@ApiResponse(responseCode = "201", description = "Voucher created")})
    public ResponseEntity<ResponseStructure<VoucherDTO>> create(@Valid @RequestBody VoucherDTO dto) {
        VoucherDTO response = voucherService.create(dto);
        return ResponseBuilder.success(HttpStatus.CREATED, "Voucher created successfully", response);
    }


    @PutMapping("/update-voucher")
    @Operation(description = "Update Voucher",
            responses = {@ApiResponse(responseCode = "200", description = "Voucher updated")})
    public ResponseEntity<ResponseStructure<VoucherDTO>> update(@PathVariable Integer id,
                                                                @Valid @RequestBody VoucherDTO dto) {
        VoucherDTO response = voucherService.update(id, dto);
        return ResponseBuilder.success(HttpStatus.OK, "Voucher updated successfully", response);
    }

    @DeleteMapping("/delete-voucher/{voucherId}")
    @Operation(description = "Delete Voucher",
            responses = {@ApiResponse(responseCode = "204", description = "Voucher deleted")})
    public ResponseEntity<ResponseStructure<Void>> delete(@PathVariable Integer voucherId) {
        voucherService.delete(voucherId);

        return (ResponseEntity<ResponseStructure<Void>>) (ResponseEntity<?>) ResponseBuilder.success(
                HttpStatus.NO_CONTENT,
                "Voucher deleted successfully",
                null
        );

    }

}
