package com.erp.Controller.TenantContoller;

import com.erp.Service.TenantService.TenantService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tenant")
public class TenantController {

    private final TenantService tenantService;

    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @DeleteMapping("/{schemaName}")
    public String deleteTenant(@PathVariable String schemaName) {
        tenantService.deleteTenant(schemaName);
        return "Tenant schema '" + schemaName + "' deleted and connections refreshed!";
    }
}
