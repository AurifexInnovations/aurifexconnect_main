package com.erp.Service.AccountDashBoard;

import com.erp.Dto.ReceiptDTO;

public interface IReceiptService {
    ReceiptDTO getById(Integer id);
    ReceiptDTO create(ReceiptDTO dto);
    ReceiptDTO update(Integer id, ReceiptDTO dto);
    void delete(Integer id);

}