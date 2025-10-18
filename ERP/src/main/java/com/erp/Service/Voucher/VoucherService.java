package com.erp.Service.Voucher;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.VoucherResponse;
import com.erp.Enum.VoucherType;
import com.erp.Model.Voucher;
import com.erp.Projection.VoucherProjection;

import java.util.List;

public interface VoucherService {

    Voucher generateFormattedVoucherId(VoucherType type);

    VoucherResponse findById(long id);

    String getFormattedVoucherId(Voucher voucher);

    List<VoucherProjection> findVouchersByFilter(FilterRequest filterRequest);

}
