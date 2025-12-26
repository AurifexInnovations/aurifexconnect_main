package com.erp.Service.InventoryService;

import com.erp.Dto.Request.InventoryRequestV2;
import com.erp.Dto.Request.InventoryUpdateRequestV2;
import com.erp.Dto.Response.DropDown;
import com.erp.Dto.Response.InventoryFormResponse;
import com.erp.Dto.Response.InventoryResponseV2;
import com.erp.Dto.Response.ResultDto;
import com.erp.Model.FileInfoDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface InventoryServiceV2 {
    ResultDto<InventoryResponseV2> addInventory(InventoryRequestV2 inventoryRequestV2, List<FileInfoDto> fileInfoDtos);

    InventoryResponseV2 deleteInventory(long itemId);

    InventoryResponseV2 updateInventory(InventoryUpdateRequestV2 inventoryRequestV2);

    ResultDto<InventoryResponseV2> getAll();

    ResultDto<InventoryResponseV2> getAllBranchWise();

    ResultDto<DropDown> getDropDown();

    ResultDto<DropDown> getDropDownEquipment();

    InventoryResponseV2 getInventoryById(Long id);

    InventoryFormResponse getInventoryFormById(Long id);
}
