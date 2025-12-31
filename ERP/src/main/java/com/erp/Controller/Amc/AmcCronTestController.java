package com.erp.Controller.Amc;


import com.erp.Service.Amc.AmcExecutionService;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dev/cron")
@RequiredArgsConstructor
public class AmcCronTestController {

    private final AmcExecutionService amcExecutionService;

    @PostMapping("/run-amc")
    public ResponseEntity<ResponseStructure<String>> runAmcCronManually(){
        amcExecutionService.processDailyAmcs();
        return ResponseBuilder.success(HttpStatus.CREATED,"AMC cron executed manually","AMC cron executed manually");
    }

}
