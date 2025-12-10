package com.erp.Controller.Inventory;

import com.erp.Dto.Request.InventoryRequestV2;
import com.erp.Dto.Request.InventoryUpdateRequestV2;
import com.erp.Dto.Response.InventoryResponse;
import com.erp.Dto.Response.InventoryResponseV2;
import com.erp.Dto.Response.ResultDto;
import com.erp.Model.Inventory;
import com.erp.Service.InventoryService.InventoryServiceImplV2;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/api")
public class InventoryControllerV2 {

    private final InventoryServiceImplV2 inventoryServiceImplV2;

    @PostMapping("/v2/inventory")
    public ResponseEntity<ResponseStructure<ResultDto<InventoryResponseV2>>> addInventoryItemV2(@RequestBody InventoryRequestV2 inventoryRequestV2){
        ResultDto<InventoryResponseV2> responseV2 = inventoryServiceImplV2.addInventory(inventoryRequestV2);
        return ResponseBuilder.success(HttpStatus.OK, "Inventory Added Successfully!!", responseV2);
    }

    @DeleteMapping("/v2/inventory")
    public ResponseEntity<ResponseStructure<InventoryResponseV2>> deleteInventoryV2(@RequestParam long itemId){
        InventoryResponseV2 responseV2 = inventoryServiceImplV2.deleteInventory(itemId);
        return ResponseBuilder.success(HttpStatus.OK, "Inventory Deleted Successfully!!", responseV2);
    }

    @PutMapping("/v2/inventory")
    public ResponseEntity<ResponseStructure<InventoryResponseV2>> updateInventoryItemV2(@RequestBody InventoryUpdateRequestV2 inventoryRequestV2){
        InventoryResponseV2 responseV2 = inventoryServiceImplV2.updateInventory(inventoryRequestV2);
        return ResponseBuilder.success(HttpStatus.OK, "Inventory Updated Successfully!!", responseV2);
    }

    @GetMapping("/v2/inventory")
    public ResponseEntity<ResponseStructure<ResultDto<InventoryResponseV2>>> getAllInventoryV2(){
        ResultDto<InventoryResponseV2> responseV2 = inventoryServiceImplV2.getAll();
        return ResponseBuilder.success(HttpStatus.OK, "Inventory Added Successfully!!", responseV2);
    }
}
