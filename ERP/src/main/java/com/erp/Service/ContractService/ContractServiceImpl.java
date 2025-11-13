package com.erp.Service.ContractService;

import com.erp.CustomRepository.ContractCustomRepository;
import com.erp.Dto.Request.ContractRequestDto;
import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.ContractResponse;
import com.erp.Dto.Response.ContractResponseDto;
import com.erp.Dto.Response.ResultDto;
import com.erp.Mapper.contractMapper.ContractMapper;
import com.erp.Model.Contract;
import com.erp.Model.GenericUser;
import com.erp.Repository.contract.ContractRepository;
import com.erp.Security.util.UserIdentity;
import com.erp.Utility.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements com.erp.Service.ContractService.ContractService {

    private final ContractRepository contractRepository;
    private final ContractMapper contractMapper;

    private final ContractCustomRepository contractCustomRepository;

    private final UserIdentity userIdentity;


    @Override
    @Transactional
    public ContractResponseDto addOrUpdateContract(ContractRequestDto request) {
        Contract contract;

        GenericUser genericUser= userIdentity.getCurrentUser();

        if (request.getId() != null) {
            log.info("Updating contract with ID: {}", request.getId());

            contract = contractRepository.findById(request.getId())
                    .orElseThrow(() -> {
                        log.error("Contract not found with ID: {}", request.getId());
                        return new RuntimeException("Contract not found with id: " + request.getId());
                    });


            // Update existing fields
            contract.setCustomerId(request.getCustomerId());
            contract.setQuotationId(request.getQuotationId());
            contract.setContractStatus(request.getContractStatus());
            contract.setStartDate(request.getStartDate());
            contract.setEndDate(request.getEndDate());
            contract.setTotalValue(request.getTotalValue());
            contract.setServiceFrequency(request.getServiceFrequency());
            contract.setPaymentTerms(request.getPaymentTerms());
            contract.setIsRecurring(request.getIsRecurring());
            contract.setActivationDate(request.getActivationDate());
            contract.setRenewalDate(request.getRenewalDate());
            contract.setContractNotes(request.getContractNotes());
            contract.setLastModifiedBy(request.getLastModifiedBy());
            contract.setLastModifiedAt(LocalDateTime.now());
            contract.setCreatedBy(genericUser.getId());
            log.debug("Contract after field updates: {}", contract);

        } else {
            log.info("Creating new contract for customer: {}", request.getCustomerId());
            request .setCreatedBy(genericUser.getId());
            contract = contractMapper.toEntity(request);
        }

        Contract savedContract = contractRepository.save(contract);
        log.info("Contract saved successfully with ID: {}", savedContract.getId());

        return contractMapper.toResponse(savedContract);
    }

    @Override
    public List<ContractResponseDto> getAllContracts() {
        log.info("Fetching all contracts from database");

        List<Contract> contracts = contractRepository.findAll();
        log.debug("Fetched {} contracts", contracts.size());

        return contracts.stream()
                .map(contractMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ResultDto<ContractResponse> getFilteredContracts(FilterRequest filterRequest) {
        log.info("Into [ContractServiceImpl] [getFilteredContracts] ");

        log.info("[ContractServiceImpl] [getFilteredContracts] :: Request :: {} ",
                ObjectMapperUtils.writeValueAsString(filterRequest));

        ResultDto<ContractResponse> contractResponseList = new ResultDto<>();

        try {
            contractResponseList = contractCustomRepository.getFilteredContracts(filterRequest);
        } catch (Exception exception) {
            log.error("Error [ContractServiceImpl] [getFilteredContracts] :: {} {} ",
                    exception.getMessage(), exception);
        }

        log.info("Exit [ContractServiceImpl] [getFilteredContracts] ");

        return contractResponseList;
    }


    @Override
    public ContractResponseDto getContractById(Long id) {
        log.info("Fetching contract by ID: {}", id);

        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Contract not found with ID: {}", id);
                    return new RuntimeException("Contract not found with id: " + id);
                });

        return contractMapper.toResponse(contract);
    }

    @Override
    @Transactional
    public void deleteContractById(Long id) {
        log.info("Deleting contract with ID: {}", id);

        if (!contractRepository.existsById(id)) {
            log.warn("Attempted to delete non-existing contract ID: {}", id);
            throw new RuntimeException("Contract not found with id: " + id);
        }

        contractRepository.deleteById(id);
        log.info("Contract deleted successfully with ID: {}", id);
    }
}
