package com.erp.Controller;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Request.UpdateVendorRequestDTO;
import com.erp.Dto.Request.VendorRequestDTO;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.VendorResponseDTO;
import com.erp.Service.Vendor.VendorService;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/vendors")
public class VendorController {

    @Autowired
    private VendorService vendorService;

    // ------------------------ CREATE ------------------------
    @PostMapping("/create")
    public ResponseEntity<ResponseStructure<VendorResponseDTO>> createVendor(
            @RequestBody @Valid VendorRequestDTO dto) {

        log.info("START :: createVendor() with request: {}", dto);

        VendorResponseDTO response = vendorService.createVendor(dto);

        log.info("END :: createVendor() => Vendor created with ID: {}", response.getVendorId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseStructure.<VendorResponseDTO>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Vendor created successfully")
                        .data(response)
                        .build());
    }

    // ------------------------ LIST (FILTER) ------------------------
    @PostMapping("/filter")
    public ResponseEntity<ResponseStructure<ResultDto<VendorResponseDTO>>> getVendorDetails(
            @RequestBody FilterRequest filterRequest
    ) {
        log.info("START :: getVendorDetails() with filterRequest: {}", filterRequest);

        ResultDto<VendorResponseDTO> vendorResponses = vendorService.getFilteredVendors(filterRequest);

        log.info("END :: getVendorDetails() => totalRecords: {}", vendorResponses.getCount());

        return ResponseBuilder.success(HttpStatus.OK, "Vendors retrieved successfully!", vendorResponses);
    }

    // ------------------------ DETAIL ------------------------
    @GetMapping("/get/{id}")
    public ResponseEntity<ResponseStructure<VendorResponseDTO>> getVendor(@PathVariable Long id) {

        log.info("START :: getVendor() for ID: {}", id);

        VendorResponseDTO response = vendorService.getVendorById(id);

        log.info("END :: getVendor() => Vendor Name: {}", response.getVendorName());

        return ResponseEntity.ok(
                ResponseStructure.<VendorResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("Vendor details fetched successfully")
                        .data(response)
                        .build()
        );
    }

    // ------------------------ UPDATE ------------------------
    @PutMapping("update/{id}")
    public ResponseEntity<ResponseStructure<VendorResponseDTO>> updateVendor(
            @PathVariable Long id,
            @RequestBody @Valid UpdateVendorRequestDTO dto) {

        log.info("START :: updateVendor() for ID: {}, request: {}", id, dto);

        VendorResponseDTO response = vendorService.updateVendor(id, dto);

        log.info("END :: updateVendor() => Vendor updated for ID: {}", id);

        return ResponseEntity.ok(
                ResponseStructure.<VendorResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("Vendor updated successfully")
                        .data(response)
                        .build()
        );
    }

    // ------------------------ DEACTIVATE ------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseStructure<String>> deleteVendor(@PathVariable Long id) {

        log.info("START :: deleteVendor() for ID: {}", id);

        vendorService.deactivateVendor(id);

        log.info("END :: deleteVendor() => Vendor marked inactive for ID: {}", id);

        return ResponseEntity.ok(
                ResponseStructure.<String>builder()
                        .status(HttpStatus.OK.value())
                        .message("Vendor deactivated successfully")
                        .data("Vendor has been marked inactive")
                        .build()
        );
    }
}
