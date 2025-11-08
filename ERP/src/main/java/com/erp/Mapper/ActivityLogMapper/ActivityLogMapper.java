package com.erp.Mapper.ActivityLogMapper;


import com.erp.Dto.Request.ActivityDto;
import com.erp.Model.Activity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class ActivityLogMapper {

    public Activity map(ActivityDto activityDto){
        return update(null, activityDto);
    }

    public Activity update(Activity activity , ActivityDto activityDto){

        if(Objects.isNull(activity)){
            activity = new Activity();
        }

        if(Objects.nonNull(activityDto)){
            activity.setAction(activityDto.getAction());
            activity.setQuantity(activityDto.getQuantity());
            activity.setPerformedBy(activityDto.getPerformedBy());
        }

        return activity;
    }

    public ActivityDto map(Activity activity){
        return update(null , activity);
    }

    public ActivityDto update(ActivityDto activityDto , Activity activity){
        if(Objects.isNull(activityDto)){
            activityDto = new ActivityDto();
        }

        if(Objects.nonNull(activity)){
            activityDto.setId(activity.getId());
            activityDto.setAction(activity.getAction());
            activityDto.setQuantity(activity.getQuantity());
            activityDto.setPerformedBy(activity.getPerformedBy());
            activityDto.setTimeStamp(activity.getTimeStamp());
        }

        return activityDto;
    }

    public List<ActivityDto> map(List<Activity> activities){
        return update( null , activities);
    }

    public List<ActivityDto> update(List<ActivityDto> activityDtos , List<Activity> activities){
        if(Objects.isNull(activityDtos)){
            activityDtos = new ArrayList<>(activities.size());
        }

        if(Objects.nonNull(activities)){
            for (Activity activity : activities){
                activityDtos.add(map(activity));
            }
        }

        return activityDtos;
    }

}
