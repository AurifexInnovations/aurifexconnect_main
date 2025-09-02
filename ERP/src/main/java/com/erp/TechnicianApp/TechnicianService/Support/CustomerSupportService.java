

package com.erp.TechnicianApp.TechnicianService.Support;

import com.erp.TechnicianApp.TechnicianDto.Request.CustomerSupportDTO;
import com.erp.TechnicianApp.TechnicianModel.CustomerSupport;

import java.util.List;

public interface CustomerSupportService {
    CustomerSupport createSupport(CustomerSupportDTO dto);
    CustomerSupport updateStatus(Long id, CustomerSupport.Status status);
    CustomerSupport getById(Long id);
    List<CustomerSupport> getAll(Long technicianId, CustomerSupport.Status status, CustomerSupport.Priority priority);
    CustomerSupport addFeedback(Long id, String feedback);
}
