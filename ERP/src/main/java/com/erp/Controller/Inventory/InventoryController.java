package com.erp.Controller.Inventory;

import com.erp.Dto.Request.CommanParam;
import com.erp.Dto.Request.InventoryRequest;
import com.erp.Dto.Response.InventoryResponse;
import com.erp.Service.InventoryService.InventoryService;
import com.erp.Utility.ListResponseStructure;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import com.erp.Utility.SimpleErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/")
@Tag(name = "Inventory Controller",description = "Collection of APIs Endpoints Dealing with Inventory Data")
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("inventory")
    @Operation(description = """
            The API Endpoints to Add Inventory Items
            """,
            responses = {
                    @ApiResponse(responseCode = "201",description = "Created Successfully")
            })
    public ResponseEntity<ResponseStructure<InventoryResponse>> addItem(@Valid @RequestBody InventoryRequest inventoryRequest){
        InventoryResponse inventoryResponse = inventoryService.addItem(inventoryRequest);
        return ResponseBuilder.success(HttpStatus.CREATED,"Inventory Created",inventoryResponse);
    }

    @PutMapping("inventory")
    @Operation(description = """
            The API Endpoints to Update Inventory Items
            """,
            responses = {
                    @ApiResponse(responseCode = "200",description = "Updated Successfully"),
                    @ApiResponse(responseCode = "404",description = "Invalid Inventory Id",content = {
                            @Content(schema = @Schema(implementation = SimpleErrorResponse.class))
                    })
            })
    public ResponseEntity<ResponseStructure<InventoryResponse>> updateItem(@RequestBody InventoryRequest inventoryRequest){
        InventoryResponse inventoryResponse = inventoryService.updateItem(inventoryRequest);
        return ResponseBuilder.success(HttpStatus.OK,"Inventory updated successfully!!",inventoryResponse);
    }

    @PostMapping("inventory-byid")
    @Operation(description = """
            The API Endpoints to Find Inventory By Item Id
            """,
            responses = {
                    @ApiResponse(responseCode = "200",description = "Found Successfully"),
                    @ApiResponse(responseCode = "404",description = "Invalid Inventory Id",content = {
                            @Content(schema = @Schema(implementation = SimpleErrorResponse.class))
                    })
            })
    public ResponseEntity<ListResponseStructure<InventoryResponse>> findByItemId(@RequestBody CommanParam id){
        List<InventoryResponse> response = inventoryService.findByItemIdOrName(id);
        return ResponseBuilder.success(HttpStatus.OK,"Inventory Found Successfully",response);
    }

    @DeleteMapping("inventory-delete")
    @Operation(description = """
            The API Endpoints to Delete Inventory By Item Id
            """,
            responses = {
                    @ApiResponse(responseCode = "200",description = "Deleted Successfully"),
                    @ApiResponse(responseCode = "404",description = "Invalid Inventory Id",content = {
                            @Content(schema = @Schema(implementation = SimpleErrorResponse.class))
                    })
            })
    public ResponseEntity<ResponseStructure<InventoryResponse>> deleteByItemId(@RequestBody InventoryRequest inventoryRequest) {
        InventoryResponse response = inventoryService.deleteByItemId(inventoryRequest);
        return ResponseBuilder.success(HttpStatus.OK,"Inventory deleted successfully!!",response);

    }


    @GetMapping("inventory/all")
    @Operation(description = """
            The API Endpoints to Found All_Inventory 
            """,
            responses = {
                    @ApiResponse(responseCode = "200",description = "Found Successfully"),
                    @ApiResponse(responseCode = "404",description = "Invalid Item Name",content = {
                            @Content(schema = @Schema(implementation = SimpleErrorResponse.class))
                    })
            })
    public ResponseEntity<ListResponseStructure<InventoryResponse>> findByAllInventory(){
        List<InventoryResponse> inventoryResponse = inventoryService.findByAll();
        return ResponseBuilder.success(HttpStatus.OK,"Inventories Found Successfully!!", inventoryResponse);
    }

    @GetMapping("/categories")
    public ResponseEntity<ListResponseStructure<String>> fetchAllCategories(){
        List<String> categories = inventoryService.fetchAllCategories();
        return ResponseBuilder.success(HttpStatus.OK,"Categories Fetched Successfully",categories);
    }
}
