package com.erp.Service.Voucher;

import com.erp.CustomRepository.VoucherCustomRepository;
import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.VoucherResponse;
import com.erp.Enum.VoucherType;
import com.erp.Exception.Voucher.VoucherNotFound;
import com.erp.Mapper.Voucher.VoucherMapper;
import com.erp.Model.Voucher;
import com.erp.Projection.VoucherProjection;
import com.erp.Repository.Voucher.VoucherRepository;
import com.erp.Utility.ObjectMapperUtils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class VoucherServiceImpl implements VoucherService{

    private final VoucherRepository voucherRepository;
    private final VoucherMapper voucherMapper;
    private final VoucherCustomRepository voucherCustomRepository;

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

        return voucherMapper.mapToVoucherResponse(voucher);
    }

    @Override
    public ResultDto<VoucherProjection> findVouchersByFilter(FilterRequest filterRequest) {
        log.info("Into [VoucherServiceImpl] [findVouchersByFilter]");
        log.info("[VoucherServiceImpl] [findVouchersByFilter] :: Request {}",
                ObjectMapperUtils.writeValueAsString(filterRequest));

        ResultDto<VoucherProjection> resultDto = new ResultDto<>();

        if (filterRequest == null) {
            throw new VoucherNotFound("Filter data is required!");
        }

        try {
            // Fetch data from repository
            resultDto = voucherCustomRepository.getVoucherDetails(filterRequest);

            if (resultDto == null || resultDto.getResults() == null || resultDto.getResults().isEmpty()) {
                throw new VoucherNotFound("No vouchers found for the given filter!");
            }

        } catch (VoucherNotFound e) {
            log.warn("VoucherNotFound in [findVouchersByFilter]: {}", e.getMessage());
            throw e;

        } catch (Exception e) {
            log.error("Error [VoucherServiceImpl] [findVouchersByFilter] :: {} :: {}", e.getMessage(), e);
            throw new VoucherNotFound("Error while fetching vouchers: " + e.getMessage());
        }

        log.info("Exit [VoucherServiceImpl] [findVouchersByFilter] with {} result(s)",
                resultDto.getResults().size());
        return resultDto;
    }


}