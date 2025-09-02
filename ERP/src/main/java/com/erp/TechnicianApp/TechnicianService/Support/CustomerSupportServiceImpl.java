package com.erp.TechnicianApp.TechnicianService.Support;

import com.erp.Exception.User.UserNotFoundException;
import com.erp.TechnicianApp.TechnicianDto.Request.CustomerSupportDTO;
import com.erp.TechnicianApp.TechnicianException.SupportException.SupportNotFoundException;
import com.erp.TechnicianApp.TechnicianModel.CustomerSupport;
import com.erp.TechnicianApp.TechnicianRepository.CustomerSupportRepository;
import com.erp.Model.User;
import com.erp.Repository.User.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerSupportServiceImpl implements CustomerSupportService {

    private final CustomerSupportRepository supportRepository;
    private final UserRepository userRepository;

    @Override
    public CustomerSupport createSupport(CustomerSupportDTO dto) {
        CustomerSupport support = new CustomerSupport();

        // Fetch the technician (User) by ID
        User technician = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("Technician not found with ID: " + dto.getUserId()));
        support.setUser(technician);

        // Set other fields
        support.setCustomerName(dto.getCustomerName());
        support.setCustomerContact(dto.getCustomerContact());
        support.setIssueDescription(dto.getIssueDescription());
        support.setPriority(dto.getPriority());
        support.setStatus(CustomerSupport.Status.OPEN);

        return supportRepository.save(support);
    }

    @Override
    public CustomerSupport updateStatus(Long id, CustomerSupport.Status status) {
        CustomerSupport support = supportRepository.findById(id)
                .orElseThrow(() -> new SupportNotFoundException("Support request not found with ID: " + id));

        support.setStatus(status);

        if (status == CustomerSupport.Status.RESOLVED || status == CustomerSupport.Status.CLOSED) {
            support.setResolvedAt(LocalDateTime.now());
        }

        return supportRepository.save(support);
    }

    @Override
    public CustomerSupport getById(Long id) {
        return supportRepository.findById(id)
                .orElseThrow(() -> new SupportNotFoundException("Support request not found with ID: " + id));
    }

    @Override
    public List<CustomerSupport> getAll(Long technicianId, CustomerSupport.Status status, CustomerSupport.Priority priority) {
        List<CustomerSupport> result = new ArrayList<>();

        if (technicianId != null) {
            result.addAll(supportRepository.findByUser_Id(technicianId));
        }
        if (status != null) {
            result.addAll(supportRepository.findByStatus(status));
        }
        if (priority != null) {
            result.addAll(supportRepository.findByPriority(priority));
        }

        if (technicianId == null && status == null && priority == null) {
            result = supportRepository.findAll();
        }

        return result;
    }

    @Override
    public CustomerSupport addFeedback(Long id, String feedback) {
        CustomerSupport support = supportRepository.findById(id)
                .orElseThrow(() -> new SupportNotFoundException("Support request not found with ID: " + id));
        support.setFeedback(feedback);
        return supportRepository.save(support);
    }
}
