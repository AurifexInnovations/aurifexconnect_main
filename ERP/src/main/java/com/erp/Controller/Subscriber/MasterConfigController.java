package com.erp.Controller.Subscriber;

import com.erp.Dto.MasterConfigDTO;
import com.erp.Service.SubscriptionService.IMasterConfigService;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/masterconfig")
@Tag(name = "MasterConfig Controller", description = "APIs for User masterconfig")
public class MasterConfigController {

    @Autowired
    IMasterConfigService masterConfigService;

    @GetMapping("/configmap/{serviceName}")
    @Operation(description = "API to fetch a User-Subscription by user id",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User Subscription Request Created")
            })
    public ResponseEntity<ResponseStructure<MasterConfigDTO>> fetchUserSubscription(@PathVariable String serviceName) {
        MasterConfigDTO response = masterConfigService.fetchMasterConfigDataByConfigName(serviceName);
        return ResponseBuilder.success(HttpStatus.CREATED, "Retreived Master Config.", response);
    }


}
