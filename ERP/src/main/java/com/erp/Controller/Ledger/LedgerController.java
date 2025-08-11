package com.erp.Controller.Ledger;

import com.erp.Dto.Request.CommanParam;
import com.erp.Dto.Request.LedgerRequest;
import com.erp.Dto.Response.LedgerResponse;
import com.erp.Service.LedgerService.LedgerService;
import com.erp.Utility.ListResponseStructure;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/")
public class LedgerController {

    private final LedgerService ledgerService;

    @PostMapping("ledger")
    @Operation( summary = "Create a new Ledger",
            description = "API to create a new Ledger record",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Ledger created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data")
            }
    )
    public ResponseEntity<ResponseStructure<LedgerResponse>> createLedger(@Valid @RequestBody LedgerRequest ledgerRequest){
        LedgerResponse ledgerResponse = ledgerService.createLedger(ledgerRequest);
        return ResponseBuilder.success(HttpStatus.CREATED,"Ledger Created", ledgerResponse);
    }

    @Operation(
            summary = "Update Ledger information",
            description = "API to update existing Ledger information by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ledger updated successfully"),
                    @ApiResponse(responseCode = "404", description = "Ledger not found")
            }
    )
    @PutMapping("ledger-update")
    public ResponseEntity<ResponseStructure<LedgerResponse>> updateLedgerInfo(@RequestBody LedgerRequest ledgerRequest){
        LedgerResponse ledgerResponse = ledgerService.updateLedgerInfo(ledgerRequest);
        return ResponseBuilder.success(HttpStatus.OK,"Ledger Info Updated Successfully", ledgerResponse);
    }

    @Operation(
            summary = "Delete Ledger by ID",
            description = "API to delete a Ledger record by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ledger deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Ledger not found")
            }
    )
    @DeleteMapping("ledger-delete")
    public ResponseEntity<ResponseStructure<LedgerResponse>> deleteByLedgerId(@RequestBody LedgerRequest ledgerId){
        LedgerResponse ledgerResponse = ledgerService.deleteByLedgerId(ledgerId);
        return ResponseBuilder.success(HttpStatus.OK,"Ledger Deleted Successfully!!", ledgerResponse);
    }

    @Operation(
            summary = "Get all Ledger",
            description = "API to fetch all customers",
            responses = {
                    @ApiResponse(responseCode = "200", description = "All Ledger fetched successfully")
            }
    )
    @GetMapping("ledger")
    public ResponseEntity<ListResponseStructure<LedgerResponse>> getAllLedger(){
        List<LedgerResponse> allLedger = ledgerService.getAllLedger();
        return ResponseBuilder.success(HttpStatus.OK,"All Ledger Fetched Successfully", allLedger);
    }

    @Operation(
            summary = "Get all Ledger",
            description = "API to fetch all customers",
            responses = {
                    @ApiResponse(responseCode = "200", description = "All Ledger fetched successfully")
            }
    )
    @PostMapping("ledger/by-Id-Name")
    public ResponseEntity<ListResponseStructure<LedgerResponse>> getLedgerByName(@RequestBody CommanParam param){
        List<LedgerResponse> ledgerResponses = ledgerService.getLedgerByIdOrName(param);
        return ResponseBuilder.success(HttpStatus.OK,"All Ledger Fetched Successfully", ledgerResponses);
    }
}
