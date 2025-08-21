package com.erp.TechnicianApp.Controller.Technician;


import com.erp.TechnicianApp.Dto.Request.CommonParam;
import com.erp.TechnicianApp.Dto.Request.TechnicianRequest;
import com.erp.TechnicianApp.Dto.Response.TechnicianResponse;
import com.erp.TechnicianApp.Service.Technician.TechnicianService;
import com.erp.Utility.ListResponseStructure;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/technician")
@AllArgsConstructor
public class TechnicianController {

    private final TechnicianService technicianService;

    @PostMapping("/create")
    public ResponseEntity<ResponseStructure<TechnicianResponse>> addTechnician(@RequestBody TechnicianRequest technicianRequest){
        TechnicianResponse technicianResponse=technicianService.addTechnician(technicianRequest);
        return ResponseBuilder.success(HttpStatus.CREATED, "Technician Created", technicianResponse);
    }
    @GetMapping("/getAll")
    public ResponseEntity<ListResponseStructure<TechnicianResponse>> getAllTechnician(){
        List<TechnicianResponse> technicianResponses= technicianService.getAllTechnician();
        return ResponseBuilder.success(HttpStatus.OK, "All Technician Fetch Successfully", technicianResponses);
    }
    @PostMapping("find_byId")
    public ResponseEntity<ListResponseStructure<TechnicianResponse>> findById(@RequestBody CommonParam param){
        List<TechnicianResponse> technicianResponse=technicianService.findTechnicianById(param);
        return ResponseBuilder.success(HttpStatus.OK, "Technician Found", technicianResponse);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseStructure<TechnicianResponse>> updateById(@PathVariable long id, @RequestBody TechnicianRequest technicianRequest){
        TechnicianResponse technicianResponse=technicianService.updateTechnicianById(id, technicianRequest);
        return ResponseBuilder.success(HttpStatus.OK,"Update SuccessFull", technicianResponse);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseStructure<TechnicianResponse>> deleteById(@PathVariable long id){
        TechnicianResponse technicianResponse=technicianService.deleteTechnicianById(id);
        return ResponseBuilder.success(HttpStatus.OK,"Delete SuccessFully", technicianResponse);
    }
}
