package com.erp.TechnicianApp.TechnicianController.SupportController;

import com.erp.TechnicianApp.TechnicianDto.Request.CustomerSupportDTO;
import com.erp.TechnicianApp.TechnicianModel.CustomerSupport;
import com.erp.TechnicianApp.TechnicianService.Support.CustomerSupportService;
import com.erp.Utility.ListResponseStructure;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/support")
public class CustomerSupportController {

    private final CustomerSupportService supportService;

    @PostMapping("/create")
    public ResponseEntity<ResponseStructure<CustomerSupport>> create(@RequestBody CustomerSupportDTO dto) {
        CustomerSupport saved = supportService.createSupport(dto);
        return ResponseBuilder.success(HttpStatus.CREATED, "Support request created", saved);
    }

    @PutMapping("/update-status/{id}")
    public ResponseEntity<ResponseStructure<CustomerSupport>> updateStatus(
            @PathVariable Long id,
            @RequestParam CustomerSupport.Status status) {
        CustomerSupport updated = supportService.updateStatus(id, status);
        return ResponseBuilder.success(HttpStatus.OK, "Status updated", updated);
    }

    @PostMapping("/record/{id}")
    public ResponseEntity<ResponseStructure<CustomerSupport>> getById(@PathVariable Long id) {
        CustomerSupport support = supportService.getById(id);
        return ResponseBuilder.success(HttpStatus.OK, "Support request fetched", support);
    }

    @GetMapping("/all")
    public ResponseEntity<ListResponseStructure<CustomerSupport>> getAll(
            @RequestParam(required = false) Long technicianId,
            @RequestParam(required = false) CustomerSupport.Status status,
            @RequestParam(required = false) CustomerSupport.Priority priority) {
        List<CustomerSupport> supports = supportService.getAll(technicianId, status, priority);
        return ResponseBuilder.success(HttpStatus.OK, "All support requests fetched", supports);
    }

    @PutMapping("/feedback/{id}")
    public ResponseEntity<ResponseStructure<CustomerSupport>> addFeedback(
            @PathVariable Long id,
            @RequestParam String feedback) {
        CustomerSupport updated = supportService.addFeedback(id, feedback);
        return ResponseBuilder.success(HttpStatus.OK, "Feedback added", updated);
    }
}
