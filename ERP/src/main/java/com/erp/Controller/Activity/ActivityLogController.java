package com.erp.Controller.Activity;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.ResultDto;
import com.erp.Model.Activity;
import com.erp.Service.Activity.ActivityService;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/activity")
public class ActivityLogController {

    private final ActivityService activityService;

    @PostMapping("/filter")
    public ResponseEntity<ResponseStructure<ResultDto<Activity>>> getActivityLogDetailsDetails(@RequestBody FilterRequest filterRequest){
        ResultDto<Activity> activities = activityService.getActivitiesByFilter(filterRequest);
        return ResponseBuilder.success(HttpStatus.OK,"activity logs retrieved successfully!",activities);
    }
}
