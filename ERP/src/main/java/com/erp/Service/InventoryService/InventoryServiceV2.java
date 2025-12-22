package com.erp.Service.InventoryService;

import com.erp.Dto.Request.InventoryRequestV2;
import com.erp.Dto.Request.InventoryUpdateRequestV2;
import com.erp.Dto.Response.DropDown;
import com.erp.Dto.Response.InventoryResponseV2;
import com.erp.Dto.Response.ResultDto;

import java.util.List;

public interface InventoryServiceV2 {
    ResultDto<InventoryResponseV2> addInventory(InventoryRequestV2 inventoryRequestV2);

    InventoryResponseV2 deleteInventory(long itemId);

    InventoryResponseV2 updateInventory(InventoryUpdateRequestV2 inventoryRequestV2);

    ResultDto<InventoryResponseV2> getAll();

    ResultDto<InventoryResponseV2> getAllBranchWise();

    ResultDto<DropDown> getDropDown();
}
