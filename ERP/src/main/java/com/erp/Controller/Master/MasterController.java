package com.erp.Controller.Master;

import com.erp.Dto.Request.MasterRequest;
import com.erp.Dto.Response.MasterResponse;
import com.erp.Service.Master.MasterService;
import com.erp.Utility.ListResponseStructure;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/")
public class MasterController {

    private final MasterService masterService;

    @PostMapping("master")
    public ResponseEntity<ResponseStructure<MasterResponse>> createMaster(@RequestBody MasterRequest masterRequest) {
        MasterResponse masterResponse = masterService.createMaster(masterRequest);
        return ResponseBuilder.success(HttpStatus.CREATED,"Master Created Successfully",masterResponse);
    }
//
//    @PutMapping("update-master/{masterId}")
//    public ResponseEntity<ResponseStructure<MasterResponse>> updateMaster(@PathVariable Long masterId, @RequestBody MasterRequest request) {
//        MasterResponse masterResponse = masterService.updateMaster(masterId,request);
//        return ResponseBuilder.success(HttpStatus.OK,"Master Updated Successfully",masterResponse);
//    }

    @GetMapping("master")
    public ResponseEntity<ResponseStructure<MasterResponse>> findByMasterId(@RequestBody MasterRequest masterRequest) {
        MasterResponse masterResponse = masterService.findById(masterRequest);
        return ResponseBuilder.success(HttpStatus.OK,"Master Found Successfully",masterResponse);
    }

//    @DeleteMapping("master-delete/{masterId}")
//    public ResponseEntity<ResponseStructure<MasterResponse>> deleteMaster(@PathVariable Long masterId) {
//        MasterResponse masterResponse = masterService.deleteMaster(masterId);
//        return ResponseBuilder.success(HttpStatus.OK,"Master Deleted Successfully",masterResponse);
//    }

    @PostMapping("/master/sales-vs-purchase-comparison-summary")
    public ResponseEntity<ListResponseStructure<Map<String, Object>>> getSalesVsPurchaseComparison(@RequestBody Map<String, String> request) {
        String type = request.get("type");
        List<Map<String, Object>> result = masterService.getSalesVsPurchaseComparison(type);
        return ResponseBuilder.success(HttpStatus.OK, "Sales vs Purchase comparison fetched", result);
    }

}