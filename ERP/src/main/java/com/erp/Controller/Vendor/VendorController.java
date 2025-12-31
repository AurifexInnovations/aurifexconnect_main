package com.erp.Controller.Vendor;

import com.erp.Dto.Request.VendorRequest;
import com.erp.Model.Vendor;
import com.erp.Service.Vendor.VendorService;
import com.erp.Utility.ListResponseStructure;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendors")
@RequiredArgsConstructor
public class VendorController {

    private final VendorService vendorService;

    // CREATE VENDOR
    @PostMapping
    public ResponseEntity<ResponseStructure<Vendor>> create(@RequestBody VendorRequest request) {
        Vendor vendor = vendorService.createVendor(request);
        return ResponseBuilder.success(HttpStatus.CREATED, "Vendor created successfully", vendor);
    }

    // UPDATE VENDOR  --> /api/vendors?id=1
    @PutMapping
    public ResponseEntity<ResponseStructure<Vendor>> update(@RequestParam Long id,
                                                            @RequestBody VendorRequest request) {
        Vendor vendor = vendorService.updateVendor(id, request);
        return ResponseBuilder.success(HttpStatus.OK, "Vendor updated successfully", vendor);
    }

    // DELETE VENDOR  --> /api/vendors?id=1
    @DeleteMapping
    public ResponseEntity<ResponseStructure<String>> delete(@RequestParam Long id) {
        vendorService.deleteVendor(id);
        return ResponseBuilder.success(HttpStatus.OK, "Vendor deleted successfully", "Deleted");
    }

    // GET ALL VENDORS
    @GetMapping
    public ResponseEntity<ListResponseStructure<Vendor>> getAll() {
        List<Vendor> vendors = vendorService.getAllVendors();
        return ResponseBuilder.success(HttpStatus.OK, "Vendors fetched successfully", vendors);
    }

    // GET VENDOR BY ID  --> /api/vendors/by-id?id=1
    @GetMapping("/by-id")
    public ResponseEntity<ResponseStructure<Vendor>> getById(@RequestParam Long id) {
        Vendor vendor = vendorService.getVendorById(id);
        return ResponseBuilder.success(HttpStatus.OK, "Vendor fetched successfully", vendor);
    }
}
