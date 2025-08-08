package com.erp.Service.Voucher;

import com.erp.Dto.Response.VoucherResponse;
import com.erp.Enum.VoucherType;
import com.erp.Exception.Voucher.VoucherNotFound;
import com.erp.Mapper.Voucher.VoucherMapper;
import com.erp.Model.Voucher;
import com.erp.Repository.Voucher.VoucherRepository;
import com.erp.Service.Voucher.VoucherNotification.VoucherNotification;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
@AllArgsConstructor
public class VoucherServiceImpl implements VoucherService{

    private final VoucherRepository voucherRepository;
    private final VoucherMapper voucherMapper;
    private final VoucherNotification voucherNotification;
    @Override
    @Transactional
    public Voucher generateFormattedVoucherId(VoucherType type) {
        LocalDate today = LocalDate.now();
        LocalDate startDate = (today.getMonthValue() >= 4)
                ? LocalDate.of(today.getYear(), 4, 1)
                : LocalDate.of(today.getYear() - 1, 4, 1);
        LocalDate endDate = startDate.plusYears(1).minusDays(1);

        Voucher voucher = voucherRepository
                .findByVoucherTypeAndStartDate(type, startDate)
                .orElseGet(() -> {
                    Voucher newVoucher = new Voucher();
                    newVoucher.setVoucherType(type);
                    newVoucher.setStartDate(startDate);
                    newVoucher.setEndDate(endDate);
                    newVoucher.setVoucherIndex("000");
                    return voucherRepository.save(newVoucher);
                });

        int currentIndex = Integer.parseInt(voucher.getVoucherIndex());
        String nextIndex = String.format("%03d", currentIndex + 1);

        voucher.setVoucherIndex(nextIndex);
        voucherRepository.save(voucher);
        voucherNotification.notifyVoucherGenerated(voucher);
        return voucher;
    }

    @Override
    public String getFormattedVoucherId(Voucher voucher) {
        String financialYear = voucher.getStartDate().getYear() + "-" + voucher.getEndDate().getYear();
        return String.format("%s/%s/%s",
                voucher.getVoucherType().name(),
                financialYear,
                voucher.getVoucherIndex());
    }

    @Override
    public VoucherResponse findById(long id) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(()-> new VoucherNotFound("Voucher Not Found By This Id : "+id));
        voucherNotification.notifyVoucherFetched(voucher);
        return voucherMapper.mapToVoucherResponse(voucher);
    }

}