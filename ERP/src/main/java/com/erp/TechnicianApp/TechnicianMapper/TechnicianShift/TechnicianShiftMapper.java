package com.erp.TechnicianApp.TechnicianMapper.TechnicianShift;

import com.erp.TechnicianApp.TechnicianDto.Request.TechnicianShiftRequest;
import com.erp.TechnicianApp.TechnicianDto.Response.TechnicianShiftResponse;
import com.erp.TechnicianApp.TechnicianModel.TechnicianShift;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TechnicianShiftMapper {


    TechnicianShift toEntity(TechnicianShiftRequest request);


    @Mapping(source = "user.id", target = "userId")
    TechnicianShiftResponse toResponse(TechnicianShift shift);

    List<TechnicianShiftResponse> toResponseList(List<TechnicianShift> shifts);
}
