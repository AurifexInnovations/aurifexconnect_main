package com.erp.Repository;

import com.erp.Model.Vouchers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoucherRepository extends JpaRepository<Vouchers, Integer> {
    boolean existsByVoucherCode(String voucherCode);
}

