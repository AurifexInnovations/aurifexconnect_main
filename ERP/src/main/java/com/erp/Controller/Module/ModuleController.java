package com.erp.Controller.Module;


import com.erp.Dto.Request.ModuleDto;
import com.erp.Service.ModuleService.ModuleService;
import com.erp.Utility.ListResponseStructure;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/module")
@RequiredArgsConstructor
public class ModuleController {

    private final ModuleService moduleService;

    @PostMapping
    public ResponseEntity<ResponseStructure<ModuleDto>> addModule(@RequestBody ModuleDto moduleDto ) {
        moduleDto  = moduleService.addModule(moduleDto);
        return ResponseBuilder.success(HttpStatus.CREATED, "module request submitted", moduleDto);
    }


    @PutMapping("/id/{mId}")
    public ResponseEntity<ResponseStructure<ModuleDto>> updateModule(@PathVariable("mId") long moduleId,
                                                                     @RequestBody ModuleDto moduleDto ) {
        moduleDto  = moduleService.updateModule( moduleId , moduleDto);
        return ResponseBuilder.success(HttpStatus.OK, "module updated Successfully", moduleDto);
    }


    @PostMapping("/id/{mId}")
    public ResponseEntity<ResponseStructure<ModuleDto>> getModuleByModule(@PathVariable("mId") long moduleId) {
        ModuleDto moduleDto  = moduleService.getModuleByModuleId( moduleId);
        return ResponseBuilder.success(HttpStatus.OK, "get module by id", moduleDto);
    }

    @GetMapping
    public ResponseEntity<ListResponseStructure<ModuleDto>> getAllModules() {
        List<ModuleDto> modules  = moduleService.getModules();
        return ResponseBuilder.success(HttpStatus.OK, "module details", modules);
    }

    @DeleteMapping("/id/{mId}")
    public ResponseEntity<ResponseStructure<ModuleDto>> deleteModuleById(@PathVariable("mId") long moduleId) {
        ModuleDto moduleDto=  moduleService.deleteModule(moduleId);
        return ResponseBuilder.success(HttpStatus.OK, "module deleted successfully" , moduleDto);
    }

}
