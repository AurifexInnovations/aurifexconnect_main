package com.erp.Service.ContractService;

import com.erp.Dto.Request.ContractRequestDto;
import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.ContractResponse;
import com.erp.Dto.Response.ContractResponseDto;
import com.erp.Dto.Response.ResultDto;
import com.erp.Model.Contract;


import java.util.List;

public interface ContractService {
    ContractResponseDto addOrUpdateContract(ContractRequestDto request);


    List<ContractResponseDto> getAllContracts();


    ContractResponseDto getContractById(Long id);


    void deleteContractById(Long id);

    ResultDto<ContractResponse> getFilteredContracts(FilterRequest filterRequest);


    Contract convertQuotationToContract(Long qutationId);
}
