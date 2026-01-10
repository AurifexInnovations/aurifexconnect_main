package com.erp.Service.AccountDashBoard;

import com.erp.Dto.InvoiceDTO;

public interface IInvoiceService {
    InvoiceDTO getById(Integer id);
    InvoiceDTO create(InvoiceDTO dto);
    InvoiceDTO update(InvoiceDTO dto);
    void delete(Integer id);

}
