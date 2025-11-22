package com.erp.Service.Vendor;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Request.VendorRequestDTO;
import com.erp.Dto.Request.UpdateVendorRequestDTO;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.VendorResponseDTO;
import org.springframework.data.domain.Page;

public interface VendorService {

    VendorResponseDTO createVendor(VendorRequestDTO dto);

    ResultDto<VendorResponseDTO> getFilteredVendors(FilterRequest filterRequest);

    VendorResponseDTO getVendorById(Long id);

    VendorResponseDTO updateVendor(Long id, UpdateVendorRequestDTO dto);

    void deactivateVendor(Long id);
}
