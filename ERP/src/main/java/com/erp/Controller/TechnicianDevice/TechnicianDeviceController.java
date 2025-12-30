package com.erp.Controller.TechnicianDevice;

import com.erp.Dto.Request.TechnicianDeviceRequest;
import com.erp.Service.TechnicianDevice.TechnicianDeviceService;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class TechnicianDeviceController {

    private final TechnicianDeviceService technicianDeviceService;

    @PutMapping("/v1/technician/devicetoken")
    public ResponseEntity<ResponseStructure<String>> saveDeviceToken(@RequestBody TechnicianDeviceRequest request) {
        return ResponseBuilder.success(
                HttpStatus.OK,
                "Device stored successfully",
                technicianDeviceService.saveDeviceToken(request)
        );
    }
}
