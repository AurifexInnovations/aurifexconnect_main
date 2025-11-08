package com.erp.Service.Activity;

import com.erp.CustomRepository.ActivityCustomRepository;
import com.erp.Dto.Request.ActivityDto;
import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.ResultDto;
import com.erp.Exception.ResourceNotFoundException;
import com.erp.Mapper.ActivityLogMapper.ActivityLogMapper;
import com.erp.Model.Activity;
import com.erp.Repository.Activity.ActivityRepository;
import com.erp.Service.InventoryService.InventoryService;
import com.erp.Utility.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityService {

    private final ActivityRepository activityRepository ;
    private final ActivityCustomRepository activityCustomRepository;
    private final InventoryService inventoryService;
    private final ActivityLogMapper activityLogMapper;

    public ActivityDto addActivity(long inventoryId , ActivityDto activityDto){
        log.info("Into [ActivityService] [addActivity] ");

        log.info("[ActivityService] [addActivity] :: InventoryId :: {} :: activityRequest  {}  "
                , inventoryId , ObjectMapperUtils.writeValueAsString(activityDto));

        boolean isExist = inventoryService.findById(inventoryId);

        if(!isExist){
            throw new ResourceNotFoundException("Inventory Details not found");
        }

        Activity activity  = activityLogMapper.map(activityDto);
        activity.setInventoryId(inventoryId);
        activity.setTimeStamp(LocalDateTime.now());

        activity = activityRepository.save(activity);

        log.info("Exit [ActivityService] [addActivity] ");

        return activityLogMapper.map(activity);
    }

    public ResultDto<Activity> getActivitiesByFilter(FilterRequest filterRequest){
        log.info("Into [ActivityService] [getActivitiesByFilter] ");

        log.info("[ActivityService] [getActivitiesByFilter] :: FilterRequest :: {} " ,
                ObjectMapperUtils.writeValueAsString(filterRequest));

        ResultDto<Activity> activityResultDto =
                activityCustomRepository.getFilteredActivities(filterRequest);

        log.info("Exit [ActivityService] [getActivitiesByFilter] ");

        return activityResultDto;
    }


}
