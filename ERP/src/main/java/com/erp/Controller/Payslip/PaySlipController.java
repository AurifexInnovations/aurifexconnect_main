package com.erp.Controller.Payslip;

import com.erp.Dto.Request.CreatePayslipRequestDTO;
import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Request.PaymentRequestDTO;
import com.erp.Dto.Request.UpdatePayslipRequestDTO;
import com.erp.Dto.Response.PayslipResponseDTO;
import com.erp.Dto.Response.ResultDto;
import com.erp.Service.Service.PayslipService;

import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/hr/payslip")
public class PaySlipController {

    @Autowired
    private PayslipService payrollService;

    // ------------------------ CREATE PAYSLIPS ------------------------
    @PostMapping("/generate")
    public ResponseEntity<ResponseStructure<String>> generatePayslips(
            @RequestBody @Valid CreatePayslipRequestDTO dto) {

        log.info("START :: [PaySlipController] [generatePayslips] :: request = {}", dto);

        payrollService.generatePayslips(dto);

        log.info("END :: [PaySlipController] [generatePayslips] :: Payslips generated");

        return ResponseBuilder.success(
                HttpStatus.CREATED,
                "Payslips generated successfully",
                "Batch Completed"
        );
    }

    // ------------------------ LIST (FILTER) ------------------------
    @PostMapping("/filter")
    public ResponseEntity<ResponseStructure<ResultDto<PayslipResponseDTO>>> filterPayslips(
            @RequestBody FilterRequest filterRequest) {

        log.info("START :: [PaySlipController] [filterPayslips] :: filter = {}", filterRequest);

        ResultDto<PayslipResponseDTO> response = payrollService.listPayslips(filterRequest);

        log.info("END :: [PaySlipController] [filterPayslips] :: totalRecords = {}",
                response != null ? response.getCount() : 0);

        return ResponseBuilder.success(
                HttpStatus.OK,
                "Payslips retrieved successfully",
                response
        );
    }

    // ------------------------ PAYSLIP DETAIL ------------------------
    @GetMapping("/get/{id}")
    public ResponseEntity<ResponseStructure<PayslipResponseDTO>> getPayslipDetail(
            @PathVariable Long id) {

        log.info("START :: [PaySlipController] [getPayslipDetail] :: payslipId = {}", id);

        PayslipResponseDTO response = payrollService.getPayslipDetail(id);

        log.info("END :: [PaySlipController] [getPayslipDetail] :: response = {}", response);

        return ResponseBuilder.success(
                HttpStatus.OK,
                "Payslip details fetched",
                response
        );
    }

    // ------------------------ UPDATE PAYSLIP ------------------------
    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseStructure<PayslipResponseDTO>> updatePayslip(
            @PathVariable Long id,
            @RequestBody @Valid UpdatePayslipRequestDTO dto) {

        log.info("START :: [PaySlipController] [updatePayslip] :: id = {}, request = {}", id, dto);

        PayslipResponseDTO response = payrollService.updatePayslip(id, dto);

        log.info("END :: [PaySlipController] [updatePayslip] :: updated successfully :: id = {}", id);

        return ResponseBuilder.success(
                HttpStatus.OK,
                "Payslip updated successfully",
                response
        );
    }

    // ------------------------ PAYMENT RECORDING ------------------------
    @PostMapping("/payment/{id}")
    public ResponseEntity<ResponseStructure<PayslipResponseDTO>> recordPayment(
            @PathVariable Long id,
            @RequestBody @Valid PaymentRequestDTO dto) {

        log.info("START :: [PaySlipController] [recordPayment] :: id = {}, request = {}", id, dto);

        PayslipResponseDTO response = payrollService.recordPayment(id, dto);

        log.info("END :: [PaySlipController] [recordPayment] :: payment recorded :: id = {}", id);

        return ResponseBuilder.success(
                HttpStatus.OK,
                "Payment recorded successfully",
                response
        );
    }
}
