package com.erp.Service.Vendor;

import com.erp.Dto.Request.VendorRequest;
import com.erp.Model.Vendor;
import com.erp.Repository.Vendor.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;

    private String generateVendorCode() {
        return "VND-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    @Override
    public Vendor createVendor(VendorRequest request) {
        Vendor v = new Vendor();
        BeanUtils.copyProperties(request, v);
        v.setVendorCode(generateVendorCode());
        return vendorRepository.save(v);
    }

    @Override
    public Vendor updateVendor(Long id, VendorRequest request) {
        Vendor v = getVendorById(id);
        BeanUtils.copyProperties(request, v);
        return vendorRepository.save(v);
    }

    @Override
    public void deleteVendor(Long id) {
        vendorRepository.delete(getVendorById(id));
    }

    @Override
    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }

    @Override
    public Vendor getVendorById(Long id) {
        return vendorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));
    }
}
