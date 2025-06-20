package com.erp.Repository.Master;

import com.erp.Enum.VoucherType;
import com.erp.Model.Master;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MasterRepository extends JpaRepository<Master, Long> {

    List<Master> findByVoucherTypeAndMasterId(VoucherType voucherType, Long relatedId);
    List<Master> findByVoucherTypeInAndCreatedDateIsNotNull(List<VoucherType> types);
    List<Master> findByVoucher_VoucherTypeAndCreatedDateBetween(VoucherType voucherType, LocalDateTime start, LocalDateTime end);
}