package com.erp.Service.AccountDashBoard;

import com.erp.Dto.VoucherDTO;

public interface IVoucherService {
    VoucherDTO getById(Integer id);
    VoucherDTO create(VoucherDTO dto);
    VoucherDTO update(Integer id, VoucherDTO dto);
    void delete(Integer id);
}