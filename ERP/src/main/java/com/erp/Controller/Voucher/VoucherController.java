package com.erp.Controller.Voucher;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.VoucherResponse;
import com.erp.Enum.VoucherType;
import com.erp.Projection.VoucherProjection;
import com.erp.Service.Voucher.VoucherService;
import com.erp.Utility.ListResponseStructure;
import com.erp.Utility.ObjectMapperUtils;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/vouchers")
@Slf4j
public class VoucherController {

    private final VoucherService voucherService;

//    @PutMapping("voucher-update")
//    public ResponseEntity<ResponseStructure<VoucherResponse>> updateVoucherByType(@RequestParam VoucherType voucherType){
//        VoucherResponse voucherResponse = voucherService.generateFormattedVoucherId(voucherType);
//        return ResponseBuilder.success(HttpStatus.OK,"VoucherIndex Updated Successfully", voucherResponse);
//    }
//

//    @PutMapping("voucher/{voucherId}")
//    public ResponseEntity<ResponseStructure<VoucherResponse>> voucherFindById(@PathVariable long voucherId){
//        VoucherResponse voucherResponse = voucherService.findById(voucherId);
//        return ResponseBuilder.success(HttpStatus.OK,"VoucherIndex Found Successfully", voucherResponse);
//    }

    @PostMapping("/find")
    public ResponseEntity<ResultDto<VoucherProjection>> findVouchers(
            @RequestBody FilterRequest filterRequest) {

        log.info("Into [VoucherController] [findVouchers] :: Request {}",
                ObjectMapperUtils.writeValueAsString(filterRequest));

        ResultDto<VoucherProjection> resultDto = voucherService.findVouchersByFilter(filterRequest);

        log.info("Exit [VoucherController] [findVouchers] with {} result(s)",
                resultDto.getResults() != null ? resultDto.getResults().size() : 0);

        return ResponseEntity.ok(resultDto);
    }
}
