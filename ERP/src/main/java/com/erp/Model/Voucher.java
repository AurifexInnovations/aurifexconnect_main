package com.erp.Model;

import com.erp.Enum.VoucherType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long voucherId;

    @Enumerated(EnumType.STRING)
    private VoucherType voucherType;

    private String voucherIndex;

    private LocalDate startDate;

    private LocalDate endDate;

    @OneToMany(mappedBy = "voucher")
    private List<Master> masters;
}
