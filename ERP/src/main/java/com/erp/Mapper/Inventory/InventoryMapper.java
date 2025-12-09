package com.erp.Mapper.Inventory;

import com.erp.Dto.Request.InventoryRequest;
import com.erp.Dto.Request.InventoryRequestV2;
import com.erp.Dto.Request.InventoryUpdateRequestV2;
import com.erp.Dto.Response.InventoryResponse;
import com.erp.Dto.Response.InventoryResponseV2;
import com.erp.Model.Inventory;
import com.erp.Model.InventoryV2;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface InventoryMapper {

    /**
     * Maps an {@link InventoryRequest} to an {@link Inventory} entity.
     *
     * @param inventoryRequest The source object containing inventory data.
     * @return The mapped {@link Inventory} entity.
     */
    Inventory mapToInventory(InventoryRequest inventoryRequest);

    /**
     * Updates an existing {@link Inventory} entity with data from an {@link InventoryRequest}.
     *
     * @param inventoryRequest The source object containing updated inventory data.
     * @param inventory The target {@link Inventory} entity to be updated.
     */
    void mapToInventoryEntity(InventoryRequest inventoryRequest,@MappingTarget Inventory inventory);

    /**
     * Converts an {@link Inventory} entity to an {@link InventoryResponse} DTO.
     *
     * @param inventory The source {@link Inventory} entity.
     * @return The corresponding {@link InventoryResponse} DTO.
     */
    InventoryResponse mapToInventoryResponse(Inventory inventory);

    /**
     * Converts a list of {@link Inventory} entities to a list of {@link InventoryResponse} DTOs.
     *
     * @param inventoryList The source list of {@link Inventory} entities.
     * @return A list of corresponding {@link InventoryResponse} DTOs.
     */
    List<InventoryResponse> mapToInventoryResponse(List<Inventory> inventoryList);


    InventoryResponseV2 ToInventoryResponseV2(InventoryV2 saved);

    void convertToEntityToUpdated(InventoryV2 source, @MappingTarget InventoryV2 target);

    InventoryV2 toInventoryV2(InventoryUpdateRequestV2 inventoryRequestV2);
}
