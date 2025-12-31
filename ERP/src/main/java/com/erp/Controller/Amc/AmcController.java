package com.erp.Controller.Amc;


import com.erp.Dto.Request.AmcRequestDto;
import com.erp.Dto.Response.AmcResponseDto;
import com.erp.Dto.Response.ResultDto;
import com.erp.Service.Amc.AmcExecutionService;
import com.erp.Service.Amc.AmcService;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AmcController {

    private final AmcService amcService;


    @PutMapping("/amc/update")
    public ResponseEntity<ResponseStructure<AmcResponseDto>> updateAmcById(@RequestBody AmcRequestDto requestDto ){

        AmcResponseDto responseDto = amcService.updateAmcById(requestDto);
        return ResponseBuilder.success(HttpStatus.OK,"Amc updated successfully !!",responseDto);
    }

    @GetMapping("/amcs")
    public ResponseEntity<ResponseStructure<ResultDto<AmcResponseDto>>> getAllAmc(){

        ResultDto<AmcResponseDto> resultDto = amcService.getAllAmc();
        return ResponseBuilder.success(HttpStatus.FOUND,"List of all Amc",resultDto);
    }

    @GetMapping("/amc")
    public ResponseEntity<ResponseStructure<AmcResponseDto>> getAmcById(@RequestParam Long amcId){

        return ResponseBuilder.success(HttpStatus.FOUND,"Amc found successfully !!", amcService.getAmcById(amcId));

    }

    @GetMapping("/amcs/getAll/branch")
    public ResponseEntity<ResponseStructure<ResultDto<AmcResponseDto>>> getAllByBranchId(@RequestParam Long branchId){
        ResultDto<AmcResponseDto> resultDto = amcService.getAllByBranchId(branchId);
        return ResponseBuilder.success(HttpStatus.FOUND,"All Qutation found based on Branch",resultDto);
    }






}
