package com.erp.Controller.EnhanceQuotation;

import com.erp.Dto.Request.CommanParam;
import com.erp.Dto.Request.QuotationRequestDto;
import com.erp.Dto.Response.QuotationResponseDto;
import com.erp.Dto.Response.ResultDto;
import com.erp.Service.EnhanceQuotation.EnhanceQuotationService;
import com.erp.Utility.ListResponseStructure;
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
@RequestMapping("/api/v1/quotation")
@Slf4j
public class EnhanceQuotationController {

    private final EnhanceQuotationService enhanceQuotationService;

    @PostMapping("/add")
    public ResponseEntity<ResponseStructure<QuotationResponseDto>> addQuotation(@RequestBody QuotationRequestDto quotationRequestDto){
        QuotationResponseDto responseDto = enhanceQuotationService.addQuotation(quotationRequestDto);
        return ResponseBuilder.success(HttpStatus.CREATED,"Quotation Created !!",responseDto);
    }

    @PatchMapping("/update-status")
    public ResponseEntity<ResponseStructure<String>> updateStatus(@RequestBody CommanParam param){

        return ResponseBuilder.success(HttpStatus.OK,"Status Updated !", enhanceQuotationService.updateStatus(param));
    }

    @GetMapping("/getById")
    public ResponseEntity<ResponseStructure<QuotationResponseDto>> quotationGetById(@RequestBody CommanParam param){
        return ResponseBuilder.success(HttpStatus.FOUND,"Quotation Found By Id !!",enhanceQuotationService.quotationGetById(param));
    }

    @GetMapping("/getAll")
    public ResponseEntity<ListResponseStructure<QuotationResponseDto>> quotationGetAll(){
        List<QuotationResponseDto> quotationResponseDtos = enhanceQuotationService.quotationGetAll();
       return ResponseBuilder.success(HttpStatus.FOUND,"All Quotation Found !!",quotationResponseDtos);
    }

    @GetMapping("/getAll/branch")
    public ResponseEntity<ResponseStructure<ResultDto<QuotationResponseDto>>> getAllByBranchId(@RequestParam Long branchId){
        ResultDto<QuotationResponseDto> resultDto = enhanceQuotationService.getAllByBranchId(branchId);
        return ResponseBuilder.success(HttpStatus.FOUND,"All Qutation found based on Branch",resultDto);
    }
}
