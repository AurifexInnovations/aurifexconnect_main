package com.erp.Service.VoucherV1;

import com.erp.Dto.Request.VoucherRequestV1;
import com.erp.Model.VoucherV1;

import java.util.List;

public interface VoucherServiceV1 {
    VoucherV1 create(VoucherRequestV1 request);
    VoucherV1 getById(Long id);
    List<VoucherV1> getAll();
    VoucherV1 update(Long id, VoucherRequestV1 request);
    void delete(Long id);
}
