package com.erp.Service.Bill;

import com.erp.Dto.Request.BillRequestDTO;
import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Request.UpdateBillRequestDTO;
import com.erp.Dto.Response.BillResponseDTO;
import com.erp.Dto.Response.ResultDto;

import java.util.List;

public interface BillService {

    BillResponseDTO createBill(BillRequestDTO dto);

    BillResponseDTO getBillById(Long id);

    BillResponseDTO updateBill(Long id, UpdateBillRequestDTO dto);

    void updateBillStatus(Long id, String status);

    void deactivateBill(Long id);

    ResultDto<BillResponseDTO> getFilteredBills(FilterRequest filterRequest);

}
