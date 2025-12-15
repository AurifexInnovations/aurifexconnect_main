package com.erp.Controller.followup;

import com.erp.Dto.Request.FollowUpRequestDto;
import com.erp.Dto.Response.FollowUpResponseDto;
import com.erp.Dto.Response.ResultDto;
import com.erp.Model.FollowUpDetails;
import com.erp.Service.followup.FollowUpService;

import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import lombok.Getter;
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

//    @PostMapping("/addOrUpdate")
//    public ResponseEntity<ResponseStructure<FollowUpDetails>> addOrUpdateFollowUp(@RequestBody FollowUpRequestDto request) {
//        log.info("API called: addOrUpdateFollowUp");
//        FollowUpDetails response = followUpService.addOrUpdateFollowUp(request);
//        return ResponseBuilder.success(HttpStatus.CREATED, "followup  Created", response);
//    }

    @PostMapping("/add")
    public ResponseEntity<ResponseStructure<FollowUpResponseDto>> addFollowUp(@RequestBody FollowUpRequestDto request){
        log.info("{FollowUP Controller} :: HIT :: Adding Follow Up Api");
        return ResponseBuilder.success(HttpStatus.CREATED, "Follow Up Added !!", followUpService.addFollowUp(request));
    }

    @GetMapping
    public ResponseEntity<ResponseStructure<ResultDto<FollowUpResponseDto>>> getAllFollowUps(){
        log.info("{FollowUP Controller} :: HIT :: Find All Follow Ups Details API");
        return ResponseBuilder.success(HttpStatus.OK, "Fetched All  !!", followUpService.getAllFollowUps());
    }

    @GetMapping("/byId")
    public ResponseEntity<ResponseStructure<FollowUpResponseDto>> getById(@RequestParam long id){
        log.info("{FollowUP Controller} :: HIT :: Get By ID API");
        return ResponseBuilder.success(HttpStatus.OK, "Fetched Follow Up Details !!", followUpService.getById(id));
    }

    @DeleteMapping
    public ResponseEntity<ResponseStructure<FollowUpResponseDto>> deleteById(@RequestParam long id){
        log.info("{FollowUP Controller} :: HIT :: Delete API");
        return ResponseBuilder.success(HttpStatus.OK, "Delete Follow Up Details !!", followUpService.deleteById(id));
    }

    @PutMapping
    public ResponseEntity<ResponseStructure<FollowUpResponseDto>> updateById(@RequestBody FollowUpRequestDto request){
        log.info("{FollowUP Controller} :: HIT :: Update API");
        return ResponseBuilder.success(HttpStatus.OK, "Update Follow Up Details !!", followUpService.updateById(request));
    }
}
