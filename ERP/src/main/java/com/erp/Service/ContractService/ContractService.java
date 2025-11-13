package com.erp.Service.ContractService;

import com.erp.Dto.Request.ContractRequestDto;
import com.erp.Dto.Response.ContractResponseDto;
import com.erp.Model.Contract;


import java.util.List;

public interface ContractService {
    ContractResponseDto addOrUpdateContract(ContractRequestDto request);


    /**
     * Fetch all contracts.
     */
    List<ContractResponseDto> getAllContracts();

    /**
     * Fetch a single contract by ID.
     */
    ContractResponseDto getContractById(Long id);

    /**
     * Delete a contract by ID.
     */
    void deleteContractById(Long id);
}
