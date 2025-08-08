package com.erp.Controller.SalarySlip;

import com.erp.Dto.Request.SalaryRequest;
import com.erp.Dto.Response.SalaryResponse;
import com.erp.Service.SalaryService.SalaryService;
import com.erp.Service.SalarySlip.SalarySlipGenerator;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/salary")
@AllArgsConstructor
public class SalarySlipController {

    private final SalaryService salaryService;
    private final SalarySlipGenerator salarySlipGenerator;

    @PostMapping("/generate")
    public String generateSalary(@RequestBody @Valid SalaryRequest request, Model model) {
        SalaryResponse salary = salaryService.generateSalaryForMonth(request);
        model.addAttribute("salary", salary);
        return "salary-slip";
    }

    @PostMapping("/slip")
    public ResponseEntity<byte[]> generateSalarySlip(@RequestBody @Valid SalaryRequest request) {
        byte[] pdf = salarySlipGenerator.generateSlipPdf(request);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=SalarySlip-" + request.getUserId() + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}