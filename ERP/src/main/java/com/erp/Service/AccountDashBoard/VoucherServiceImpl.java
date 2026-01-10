package com.erp.Service.AccountDashBoard;

import com.erp.Dto.VoucherDTO;
import com.erp.Mapper.AccountDashBoard.VoucherMapper;
import com.erp.Model.Vouchers;
import com.erp.Repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoucherServiceImpl implements IVoucherService {

    @Autowired
    private VoucherRepository repository;
    
    VoucherMapper vm = new VoucherMapper();

    @Override
    public VoucherDTO getById(Integer id) {
        Vouchers entity = repository.findById(id).orElse(null);
        return entity != null ? vm.toDto(entity) : null;
    }

    @Override
    public VoucherDTO create(VoucherDTO dto) {
        Vouchers entity = vm.toEntity(dto);
        return vm.toDto(repository.save(entity));
    }

    @Override
    public VoucherDTO update(Integer id, VoucherDTO dto) {
        Vouchers entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voucher not found with id: " + id));

        // Update fields from DTO
        entity.setVoucherName(dto.getVoucherName());
        entity.setVoucherId(dto.getVoucherId());
        entity.setDefaultSeriesPrefix(dto.getDefaultSeriesPrefix());
        entity.setVoucherAppliesTo(dto.getVoucherAppliesTo());
        entity.setVoucherCode(dto.getVoucherCode());

        Vouchers updated = repository.save(entity);
        return vm.toDto(updated);
    }

    @Override
    public void delete(Integer id) {
        Vouchers entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voucher not found with id: " + id));
        repository.delete(entity);
    }

}