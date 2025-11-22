package com.erp.Service.Vendor;

import com.erp.CustomRepository.VendorCustomRepository;
import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Request.UpdateVendorRequestDTO;
import com.erp.Dto.Request.VendorRequestDTO;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.VendorResponseDTO;
import com.erp.Exception.ResourceNotFoundException;
import com.erp.Model.Vendor;
import com.erp.Repository.Vendor.VendorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class VendorServiceImpl implements VendorService {

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private VendorCustomRepository vendorCustomRepository;

    @Override
    @Transactional
    public VendorResponseDTO createVendor(VendorRequestDTO dto) {
        log.info("Into [VendorServiceImpl] [createVendor]");

        try {
            if (vendorRepository.findActiveVendorByEmail(dto.getEmailAddress()).isPresent()) {
                throw new ResourceNotFoundException("Email already exists");
            }

            Vendor vendor = new Vendor();
            vendor.setVendorName(dto.getVendorName());
            vendor.setContactPerson(dto.getContactPerson());
            vendor.setPhoneNumber(dto.getPhoneNumber());
            vendor.setEmailAddress(dto.getEmailAddress());
            vendor.setBillingAddress(dto.getBillingAddress());
            vendor.setPaymentTerms(dto.getPaymentTerms());
            vendor.setCreditLimit(dto.getCreditLimit());

            vendorRepository.save(vendor);

            log.info("Exit [VendorServiceImpl] [createVendor]");
            return mapToResponse(vendor);

        } catch (Exception ex) {
            log.error("Error [VendorServiceImpl] [createVendor] :: {} :: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public ResultDto<VendorResponseDTO> getFilteredVendors(FilterRequest filterRequest) {
        log.info("Into [VendorServiceImpl] [getFilteredVendors]");

        try {
            ResultDto<VendorResponseDTO> vendorResponses =
                    vendorCustomRepository.getFilteredVendors(filterRequest);

            log.info("Exit [VendorServiceImpl] [getFilteredVendors]");
            return vendorResponses;

        } catch (Exception exception) {
            log.error("Error [VendorServiceImpl] [getFilteredVendors] :: {} :: {}",
                    exception.getMessage(), exception);
            throw exception;
        }
    }

    @Override
    public VendorResponseDTO getVendorById(Long id) {
        log.info("Into [VendorServiceImpl] [getVendorById] :: vendorId = {}", id);

        try {
            Vendor vendor = vendorRepository.findActiveVendorById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Active vendor not found"));

            log.info("Exit [VendorServiceImpl] [getVendorById]");
            return mapToResponse(vendor);

        } catch (Exception e) {
            log.error("Error [VendorServiceImpl] [getVendorById] :: {} :: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public VendorResponseDTO updateVendor(Long id, UpdateVendorRequestDTO dto) {
        log.info("Into [VendorServiceImpl] [updateVendor] :: vendorId = {}", id);

        try {
            Vendor vendor = vendorRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));

            if (dto.getPaymentTerms() != null)
                vendor.setPaymentTerms(dto.getPaymentTerms());

            if (dto.getCreditLimit() != null)
                vendor.setCreditLimit(dto.getCreditLimit());

            vendorRepository.save(vendor);

            log.info("Exit [VendorServiceImpl] [updateVendor]");
            return mapToResponse(vendor);

        } catch (Exception ex) {
            log.error("Error [VendorServiceImpl] [updateVendor] :: {} :: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public void deactivateVendor(Long id) {
        log.info("Into [VendorServiceImpl] [deactivateVendor] :: vendorId = {}", id);

        try {
            Vendor vendor = vendorRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));

            vendor.setIsActive(false);
            vendorRepository.save(vendor);

            log.info("Exit [VendorServiceImpl] [deactivateVendor]");

        } catch (Exception ex) {
            log.error("Error [VendorServiceImpl] [deactivateVendor] :: {} :: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    private VendorResponseDTO mapToResponse(Vendor vendor) {
        VendorResponseDTO response = new VendorResponseDTO();

        response.setVendorId(vendor.getVendorId());
        response.setVendorName(vendor.getVendorName());
        response.setContactPerson(vendor.getContactPerson());
        response.setPhoneNumber(vendor.getPhoneNumber());
        response.setEmailAddress(vendor.getEmailAddress());
        response.setBillingAddress(vendor.getBillingAddress());
        response.setPaymentTerms(vendor.getPaymentTerms());
        response.setCreditLimit(vendor.getCreditLimit());
        response.setIsActive(vendor.getIsActive());
        response.setUpdatedDate(vendor.getUpdatedDate());

        return response;
    }
}
