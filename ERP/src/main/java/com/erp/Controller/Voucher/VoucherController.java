package com.erp.Controller.Voucher;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.VoucherResponse;
import com.erp.Enum.VoucherType;
import com.erp.Projection.VoucherProjection;
import com.erp.Service.Voucher.VoucherService;
import com.erp.Utility.ListResponseStructure;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/")
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

    @PostMapping("/vouchers/find")
    public ResponseEntity<ListResponseStructure<VoucherProjection>> findVouchers(
            @RequestBody FilterRequest filterRequest) {

        List<VoucherProjection> projections = voucherService.findVouchersByFilter(filterRequest);

        return ResponseBuilder.success(
                HttpStatus.OK,
                "Voucher(s) Found Successfully!",
                projections
        );
    }

}
