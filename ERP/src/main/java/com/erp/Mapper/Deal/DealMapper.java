package com.erp.Mapper.Deal;


import com.erp.Dto.Request.DealRequest;
import com.erp.Dto.Response.DealResponse;
import com.erp.Model.Deal;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DealMapper {

    DealResponse mapToResponse(Deal deal);

    List<DealResponse> mapToResponseList(List<Deal> deals);

    Deal mapToEntity(DealRequest request);

    void updateEntityFromRequest(DealRequest request, @MappingTarget Deal deal);
}
