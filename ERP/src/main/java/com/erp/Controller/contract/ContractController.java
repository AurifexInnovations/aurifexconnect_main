package com.erp.Controller.contract;

import com.erp.Dto.Request.ContractRequestDto;
import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.ContractResponse;
import com.erp.Dto.Response.ContractResponseDto;

import com.erp.Dto.Response.ResultDto;
import com.erp.Utility.ListResponseStructure;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/contracts")
public class ContractController {

    private final com.erp.Service.ContractService.ContractService contractService;


    @PostMapping
    public ResponseEntity<ResponseStructure<ContractResponseDto>> addOrUpdateContract(
            @RequestBody ContractRequestDto contractRequestDto) {
        log.info("Received request to add or update contract: {}", contractRequestDto);

        ContractResponseDto response = contractService.addOrUpdateContract(contractRequestDto);

        String message = (contractRequestDto.getId() != null)
                ? "Contract updated successfully"
                : "Contract created successfully";

        log.info("Contract processed successfully: {}", response.getId());
        return ResponseBuilder.success(HttpStatus.OK, message, response);
    }


    @GetMapping
    public ResponseEntity<ListResponseStructure<ContractResponseDto>> getAllContracts() {
        log.info("Fetching all contracts");

        List<ContractResponseDto> contracts = contractService.getAllContracts();

        log.debug("Fetched {} contracts", contracts.size());
        return ResponseBuilder.success(HttpStatus.OK, "Contract list retrieved successfully", contracts);
    }


    @PostMapping("/filter")
    public ResponseEntity<ResultDto<ContractResponse>> getFilteredContracts(@RequestBody FilterRequest filterRequest) {
        log.info("[ContractController] [getFilteredContracts] called");
        ResultDto<ContractResponse> result = contractService.getFilteredContracts(filterRequest);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/id/{contractId}")
    public ResponseEntity<ResponseStructure<ContractResponseDto>> getContractById(
            @PathVariable("contractId") Long contractId) {
        log.info("Fetching contract by ID: {}", contractId);

        ContractResponseDto response = contractService.getContractById(contractId);

        log.info("Fetched contract successfully with ID: {}", contractId);
        return ResponseBuilder.success(HttpStatus.OK, "Contract details retrieved", response);
    }


    @DeleteMapping("/id/{contractId}")
    public ResponseEntity<ResponseStructure<ContractResponseDto>> deleteContractById(
            @PathVariable("contractId") Long contractId) {
        log.info("Request to delete contract with ID: {}", contractId);

        contractService.deleteContractById(contractId);

        log.info("Deleted contract successfully with ID: {}", contractId);
        return ResponseBuilder.success(HttpStatus.OK, "Contract deleted successfully", (ContractResponseDto) null);
    }
}
