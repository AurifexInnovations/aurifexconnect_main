package com.erp.Service.VoucherV1;

import com.erp.Dto.Request.VoucherRequestV1;
import com.erp.Model.VoucherV1;
import com.erp.Repository.VoucherV1.VoucherRepositoryV1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VoucherServiceImplV1 implements VoucherServiceV1 {

    private final VoucherRepositoryV1 repository;

    @Override
    public VoucherV1 create(VoucherRequestV1 request) {
        VoucherV1 voucher = VoucherV1.builder()
                .voucherNo(request.getVoucherNo())
                .voucherType(request.getVoucherType())
                .voucherDate(request.getVoucherDate())
                .financialYear(request.getFinancialYear())
                .build();

        return repository.save(voucher);
    }

    @Override
    public VoucherV1 getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voucher not found"));
    }

    @Override
    public List<VoucherV1> getAll() {
        return repository.findAll();
    }

    @Override
    public VoucherV1 update(Long id, VoucherRequestV1 request) {
        VoucherV1 voucher = getById(id);

        voucher.setVoucherNo(request.getVoucherNo());
        voucher.setVoucherType(request.getVoucherType());
        voucher.setVoucherDate(request.getVoucherDate());
        voucher.setFinancialYear(request.getFinancialYear());

        return repository.save(voucher);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
