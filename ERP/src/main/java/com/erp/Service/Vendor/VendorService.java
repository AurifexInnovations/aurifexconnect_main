package com.erp.Service.Vendor;

import com.erp.Dto.Request.VendorRequest;
import com.erp.Model.Vendor;

import java.util.List;

public interface VendorService {
    Vendor createVendor(VendorRequest request);
    Vendor updateVendor(Long id, VendorRequest request);
    void deleteVendor(Long id);
    List<Vendor> getAllVendors();
    Vendor getVendorById(Long id);
}

