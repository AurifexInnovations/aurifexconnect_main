package com.erp.Controller.followup;

import com.erp.Dto.Request.FollowUpRequestDto;
import com.erp.Model.FollowUpDetails;
import com.erp.Service.followup.FollowUpService;

import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/followup")
@RequiredArgsConstructor
@Slf4j
public class FollowUpController {

    private final FollowUpService followUpService;

    @PostMapping("/addOrUpdate")
    public ResponseEntity<ResponseStructure<FollowUpDetails>> addOrUpdateFollowUp(@RequestBody FollowUpRequestDto request) {
        log.info("API called: addOrUpdateFollowUp");
        FollowUpDetails response = followUpService.addOrUpdateFollowUp(request);
        return ResponseBuilder.success(HttpStatus.CREATED, "followup  Created", response);
    }
}
