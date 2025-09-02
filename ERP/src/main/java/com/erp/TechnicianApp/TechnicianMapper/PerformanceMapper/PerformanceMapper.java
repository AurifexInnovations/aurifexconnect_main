package com.erp.TechnicianApp.TechnicianMapper.PerformanceMapper;

import com.erp.TechnicianApp.TechnicianDto.Response.PerformanceResponse;
import com.erp.TechnicianApp.TechnicianModel.TechnicianPerformance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring")
public interface PerformanceMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.firstName", target = "userFirstName")
    @Mapping(source = "user.lastName", target = "userLastName")
    @Mapping(source = "user.email", target = "userEmail")
    PerformanceResponse toResponse(TechnicianPerformance performance);

    List<PerformanceResponse> toResponseList(List<TechnicianPerformance> performances);
}
