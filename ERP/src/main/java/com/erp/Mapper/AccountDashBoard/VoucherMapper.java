package com.erp.Mapper.AccountDashBoard;

import com.erp.Dto.VoucherDTO;
import com.erp.Model.Vouchers;

import java.util.ArrayList;
import java.util.List;

public class VoucherMapper {

    public static VoucherDTO toDto(Vouchers entity) {
        VoucherDTO dto = new VoucherDTO();
        dto.setVoucherId(entity.getVoucherId());
        dto.setVoucherName(entity.getVoucherName());
        dto.setVoucherCode(entity.getVoucherCode());
        dto.setVoucherCategory(entity.getVoucherCategory());
        dto.setVoucherAppliesTo(entity.getVoucherAppliesTo());
        dto.setDefaultSeriesPrefix(entity.getDefaultSeriesPrefix());
        return dto;
    }

    public static Vouchers toEntity(VoucherDTO dto) {
        Vouchers entity = new Vouchers();
        entity.setVoucherId(dto.getVoucherId());
        entity.setVoucherName(dto.getVoucherName());
        entity.setVoucherCode(dto.getVoucherCode());
        entity.setVoucherCategory(dto.getVoucherCategory());
        entity.setVoucherAppliesTo(dto.getVoucherAppliesTo());
        entity.setDefaultSeriesPrefix(dto.getDefaultSeriesPrefix());
        return entity;
    }

    public static List<VoucherDTO> toDtoList(List<Vouchers> entities) {
        List<VoucherDTO> list = new ArrayList<>();
        for (Vouchers entity : entities) {
            list.add(toDto(entity));
        }
        return list;
    }
}