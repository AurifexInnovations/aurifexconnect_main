package com.erp.Service.Service;

import com.erp.Dto.Request.CreatePayslipRequestDTO;
import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Request.PaymentRequestDTO;
import com.erp.Dto.Request.UpdatePayslipRequestDTO;
import com.erp.Dto.Response.PayslipResponseDTO;
import com.erp.Dto.Response.ResultDto;

public interface PayslipService {
    PayslipResponseDTO generatePayslips(CreatePayslipRequestDTO dto);

    ResultDto<PayslipResponseDTO> listPayslips(FilterRequest filter);

    PayslipResponseDTO getPayslipDetail(Long id);

    PayslipResponseDTO updatePayslip(Long id, UpdatePayslipRequestDTO dto);

    PayslipResponseDTO recordPayment(Long id, PaymentRequestDTO dto);
}
