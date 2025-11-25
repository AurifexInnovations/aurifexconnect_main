package com.erp.Service.Service;

import com.erp.CustomRepository.PayslipCustomRepository;
import com.erp.Dto.Request.CreatePayslipRequestDTO;
import com.erp.Dto.Request.EmployeeDeductionDTO;
import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Request.PaymentRequestDTO;
import com.erp.Dto.Request.UpdatePayslipRequestDTO;

import com.erp.Dto.Response.PayslipResponseDTO;
import com.erp.Dto.Response.ResultDto;

import com.erp.Exception.ResourceNotFoundException;

import com.erp.Model.Payslip;
import com.erp.Model.PayslipDeduction;

import com.erp.Repository.PaySlip.PayslipDeductionRepository;
import com.erp.Repository.PaySlip.PayslipRepository;

import lombok.extern.slf4j.Slf4j;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PaySlipServiceImpl implements PayslipService {

    @Autowired
    private PayslipRepository payslipRepo;

    @Autowired
    private PayslipDeductionRepository deductionRepo;

    @Autowired
    private PayslipCustomRepository customRepo;

    // --------------------------------------------------------------------------------------------
    // 1. Generate Payslip (Single Employee - Employee Logic Removed)
    // --------------------------------------------------------------------------------------------
    @Override
    @Transactional
    public PayslipResponseDTO generatePayslips(CreatePayslipRequestDTO dto) {

        log.info("START :: [PaySlipServiceImpl] [generatePayslips]");

        try {
            // Gross salary from request (or can be added dynamically)
            BigDecimal gross = new BigDecimal("3000.00");

            // Total deduction calculation
            BigDecimal totalDeduction = dto.getEmployeeDeductions().stream()
                    .map(EmployeeDeductionDTO::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal net = gross.subtract(totalDeduction);

            Payslip payslip = new Payslip();
          //  payslip.setEmployeeId(dto.getEmployeeId());   // now taken directly from request
            payslip.setPayPeriodStart(dto.getPayPeriodStart());
            payslip.setPayPeriodEnd(dto.getPayPeriodEnd());
            payslip.setGenerationDate(dto.getGenerationDate());
            payslip.setGrossSalary(gross);
            payslip.setTotalDeductions(totalDeduction);
            payslip.setNetPay(net);
            payslip.setVoucherId(dto.getVoucherId());
            payslip.setStatus("Generated");

            payslipRepo.save(payslip);

            // Generate payslip number
            payslip.setPayslipNumber(
                    "PS-" + LocalDate.now().getYear() + "-" +
                            String.format("%04d", payslip.getPayslipId())
            );
            payslipRepo.save(payslip);

            // Save deductions
            List<PayslipDeduction> dedEntities = new ArrayList<>();

            for (EmployeeDeductionDTO d : dto.getEmployeeDeductions()) {

                PayslipDeduction pd = new PayslipDeduction();
                pd.setPayslipId(payslip.getPayslipId());
                pd.setDeductionType(d.getDeductionType());
                pd.setAmount(d.getAmount());

                dedEntities.add(pd);
            }

            deductionRepo.saveAll(dedEntities);

            log.info("END :: Payslip generated successfully");

            return mapToResponse(payslip, dedEntities);

        } catch (Exception ex) {
            log.error("ERROR :: [generatePayslips] :: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    // --------------------------------------------------------------------------------------------
    // 2. Filters
    // --------------------------------------------------------------------------------------------
    @Override
    public ResultDto<PayslipResponseDTO> listPayslips(FilterRequest filter) {

        log.info("START :: [PaySlipServiceImpl] [listPayslips]");

        ResultDto<PayslipResponseDTO> response = customRepo.filterPayslips(filter);

        log.info("END :: [listPayslips]");
        return response;
    }

    // --------------------------------------------------------------------------------------------
    // 3. View Detail
    // --------------------------------------------------------------------------------------------
    @Override
    public PayslipResponseDTO getPayslipDetail(Long id) {

        log.info("START :: [PaySlipServiceImpl] [getPayslipDetail] :: id = {}", id);

        Payslip p = payslipRepo.findByPayslipIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payslip not found"));

        List<PayslipDeduction> ded = deductionRepo.findByPayslipId(id);

        log.info("END :: [getPayslipDetail]");
        return mapToResponse(p, ded);
    }

    // --------------------------------------------------------------------------------------------
    // 4. Update Payslip
    // --------------------------------------------------------------------------------------------
    @Override
    @Transactional
    public PayslipResponseDTO updatePayslip(Long id, UpdatePayslipRequestDTO dto) {

        log.info("START :: [PaySlipServiceImpl] [updatePayslip] :: id = {}", id);

        Payslip p = payslipRepo.findByPayslipIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payslip not found"));

        deductionRepo.deleteAll(deductionRepo.findByPayslipId(id));

        List<PayslipDeduction> newList = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (EmployeeDeductionDTO d : dto.getEmployeeDeductions()) {
            PayslipDeduction pd = new PayslipDeduction();
            pd.setPayslipId(id);
            pd.setDeductionType(d.getDeductionType());
            pd.setAmount(d.getAmount());
            newList.add(pd);
            total = total.add(d.getAmount());
        }

        deductionRepo.saveAll(newList);

        p.setTotalDeductions(total);
        p.setNetPay(p.getGrossSalary().subtract(total));

        payslipRepo.save(p);

        log.info("END :: [updatePayslip]");
        return mapToResponse(p, newList);
    }

    // --------------------------------------------------------------------------------------------
    // 5. Record Payment
    // --------------------------------------------------------------------------------------------
    @Override
    @Transactional
    public PayslipResponseDTO recordPayment(Long id, PaymentRequestDTO dto) {

        log.info("START :: [PaySlipServiceImpl] [recordPayment] :: id = {}", id);

        Payslip p = payslipRepo.findByPayslipIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payslip not found"));

        p.setStatus("Paid");
        payslipRepo.save(p);

        log.info("END :: [recordPayment]");
        return getPayslipDetail(id);
    }

    // --------------------------------------------------------------------------------------------
    // 6. Mapping
    // --------------------------------------------------------------------------------------------
    private PayslipResponseDTO mapToResponse(Payslip p, List<PayslipDeduction> ded) {

        PayslipResponseDTO res = new PayslipResponseDTO();

        res.setPayslipId(p.getPayslipId());
        res.setEmployeeId(p.getEmployeeId());
        res.setPayPeriodStart(p.getPayPeriodStart());
        res.setPayPeriodEnd(p.getPayPeriodEnd());
        res.setGenerationDate(p.getGenerationDate());
        res.setGrossSalary(p.getGrossSalary());
        res.setTotalDeductions(p.getTotalDeductions());
        res.setNetPay(p.getNetPay());
        res.setStatus(p.getStatus());
        res.setPayslipNumber(p.getPayslipNumber());
        res.setVoucherId(p.getVoucherId());

        List<EmployeeDeductionDTO> list = new ArrayList<>();

        for (PayslipDeduction d : ded) {
            EmployeeDeductionDTO dto = new EmployeeDeductionDTO();
            dto.setDeductionType(d.getDeductionType());
            dto.setAmount(d.getAmount());
            list.add(dto);
        }

        res.setEmployeeDeductions(list);
        return res;
    }
}
