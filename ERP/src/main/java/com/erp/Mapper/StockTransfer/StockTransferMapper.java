package com.erp.Mapper.StockTransfer;

import com.erp.Dto.Request.StockTransferRequest;
import com.erp.Dto.Response.StockTransferResponse;
import com.erp.Model.StockTransfer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface StockTransferMapper {

    // Map request DTO to entity
    StockTransfer mapToStockTransfer(StockTransferRequest request);

    // Map entity to response DTO
    @Mapping(source = "fromBranch.branchName", target = "fromBranchName")
    @Mapping(source = "toBranch.branchName", target = "toBranchName")
    @Mapping(source = "inventory.itemName", target = "itemName")
    StockTransferResponse mapToStockTransferResponse(StockTransfer stockTransfer);

    // Map list of entities to list of response DTOs
    List<StockTransferResponse> mapToStockTransferResponse(List<StockTransfer> stockTransfers);
}
