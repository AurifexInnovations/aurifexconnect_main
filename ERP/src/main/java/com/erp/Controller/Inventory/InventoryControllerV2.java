package com.erp.Controller.Inventory;

import com.erp.Dto.Request.InventoryRequestV2;
import com.erp.Dto.Request.InventoryUpdateRequestV2;
import com.erp.Dto.Response.*;
import com.erp.Model.Inventory;
import com.erp.Service.InventoryService.InventoryServiceImplV2;
import com.erp.Service.InventoryService.InventoryServiceV2;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/api")
public class InventoryControllerV2 {

    private final InventoryServiceV2 inventoryServiceImplV2;

    @PostMapping(
            value = "/v2/inventory",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ResponseStructure<ResultDto<InventoryResponseV2>>>
    addInventoryItemV2(
            @RequestPart("inventory") String inventory,
            @RequestPart(value = "files", required = false) MultipartFile[] files
    ) throws JsonProcessingException {
        ResultDto<InventoryResponseV2> responseV2 =
                inventoryServiceImplV2.addInventory(inventory, files);

        return ResponseBuilder.success(
                HttpStatus.OK,
                "Inventory Added Successfully!!",
                responseV2
        );
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

    @GetMapping("/v2/inventory/branchWise")
    public ResponseEntity<ResponseStructure<ResultDto<InventoryResponseV2>>> getAllInventoryV2BranchWise(){
        ResultDto<InventoryResponseV2> responseV2 = inventoryServiceImplV2.getAllBranchWise();
        return ResponseBuilder.success(HttpStatus.OK, "Inventory Fetched Successfully!!", responseV2);
    }

    @GetMapping("/v2/inventory/dropdown")
    public ResponseEntity<ResponseStructure<ResultDto<DropDown>>> getAllInventoryV2DropDown(){
        ResultDto<DropDown> resultDto = inventoryServiceImplV2.getDropDown();
        return ResponseBuilder.success(HttpStatus.OK, "Product Drop Down Fetched !!", resultDto);
    }

    @GetMapping("/v2/inventory/dropdown/equipment")
    public ResponseEntity<ResponseStructure<ResultDto<DropDown>>> getAllEquipmentDropDown(){
        ResultDto<DropDown> resultDto = inventoryServiceImplV2.getDropDownEquipment();
        return ResponseBuilder.success(HttpStatus.OK, "Equipment Drop Down Fetched !!", resultDto);
    }

    @GetMapping("/v2/inventory/byId")
    public ResponseEntity<ResponseStructure<InventoryResponseV2>> getInventoryById(@RequestParam Long id){
        InventoryResponseV2 resultDto = inventoryServiceImplV2.getInventoryById(id);
        return ResponseBuilder.success(HttpStatus.OK, "Expected Inventory Fetched !!", resultDto);
    }

    @GetMapping("/v2/inventory/form/byId")
    public ResponseEntity<ResponseStructure<InventoryFormResponse>> getInventoryByIdForm(@RequestParam Long id){
        InventoryFormResponse resultDto = inventoryServiceImplV2.getInventoryFormById(id);
        return ResponseBuilder.success(HttpStatus.OK, "Expected Inventory Fetched !!", resultDto);
    }
}
