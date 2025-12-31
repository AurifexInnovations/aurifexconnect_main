package com.erp.Mapper.Amc;

import com.erp.Dto.Response.AmcResponseDto;
import com.erp.Model.Amc;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AmcMapper {

    @Mapping(source = "branch.branchId", target = "branchId")
    @Mapping(source = "salesOrder.salesOrderNumber", target = "salesOrderId")
    @Mapping(source = "customerDetails.id", target = "customerId")
    AmcResponseDto toAmcDto(Amc amc);

}
