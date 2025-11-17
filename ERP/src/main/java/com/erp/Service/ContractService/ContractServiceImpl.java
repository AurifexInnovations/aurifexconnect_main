package com.erp.Service.ContractService;

import com.erp.CustomRepository.ContractCustomRepository;
import com.erp.Dto.Request.ContractRequestDto;
import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.ContractResponse;
import com.erp.Dto.Response.ContractResponseDto;
import com.erp.Dto.Response.ResultDto;
import com.erp.Enum.ContractStatus;
import com.erp.Enum.ServiceFrequency;
import com.erp.Exception.ResourceNotFoundException;
import com.erp.Mapper.contractMapper.ContractMapper;
import com.erp.Model.Contract;
import com.erp.Model.GenericUser;
import com.erp.Model.Quotation;
import com.erp.Repository.Quotation.QuotationRepository;
import com.erp.Repository.contract.ContractRepository;
import com.erp.Security.util.UserIdentity;
import com.erp.Utility.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements com.erp.Service.ContractService.ContractService {

    private final ContractRepository contractRepository;
    private final ContractMapper contractMapper;
    private final QuotationRepository quotationRepository;

    private final ContractCustomRepository contractCustomRepository;

    private final UserIdentity userIdentity;

    @Override
    @Transactional
    public ContractResponseDto addOrUpdateContract(ContractRequestDto request) {
        try {
            Contract contract;
            GenericUser genericUser = userIdentity.getCurrentUser();

            if (request.getId() != null) {
                log.info("Updating contract with ID: {}", request.getId());

                contract = contractRepository.findById(request.getId())
                        .orElseThrow(() -> {
                            log.error("Contract not found with ID: {}", request.getId());
                            return new ResourceNotFoundException("Contract not found with id: " + request.getId());
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
              //  contract.setLastModifiedBy(genericUser.getId());
                contract.setLastModifiedAt(LocalDateTime.now());
                log.debug("Contract after field updates: {}", contract);

            } else {
                log.info("Creating new contract for customer: {}", request.getCustomerId());
                request.setCreatedBy(genericUser.getId());
                contract = contractMapper.toEntity(request);
            }

            Contract savedContract = contractRepository.save(contract);
            log.info("Contract saved successfully with ID: {}", savedContract.getId());

            return contractMapper.toResponse(savedContract);

        } catch (ResourceNotFoundException ex) {
            log.error("Error while updating contract: {}", ex.getMessage(), ex);
            throw ex;

        } catch (Exception ex) {
            log.error("Unexpected error occurred in addOrUpdateContract: {}", ex.getMessage(), ex);
            throw new RuntimeException("Something went wrong while processing contract", ex);
        }
    }


    @Override
    public ResultDto<ContractResponseDto> getAllContracts() {
        log.info("Fetching all contracts from database");

        List<Contract> contracts = contractRepository.findAll();
        log.debug("Fetched {} contracts", contracts.size());

        ResultDto<ContractResponseDto> resultDto = new ResultDto<>();

        resultDto.setResults(contracts != null ? contractMapper.toContractResponseList(contracts) : List.of());
        resultDto.setCount(contracts != null ? contracts.size() : 0);

        return resultDto;
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

    public Contract convertQuotationToContract(Long quotationId) {
        Optional<Quotation> quotationLocal = quotationRepository.findById(quotationId);
        if (quotationLocal.isEmpty()){
            throw new ResourceNotFoundException("Quotation not found with ID: " + quotationId);
        }
        Quotation quotation = quotationLocal.get();

        GenericUser currentUser = userIdentity.getCurrentUser();

        // Convert quotation data into contract
        Contract contract = Contract.builder()
                .quotationId(quotation.getId())
                .customerId(quotation.getCustomerId())
                .contractStatus(ContractStatus.DRAFT)
                .startDate(LocalDate.now()) // set as current date or based on business logic
                .endDate(quotation.getValidityDate() != null ? quotation.getValidityDate() : LocalDate.now().plusMonths(6))
                .totalValue(BigDecimal.valueOf(quotation.getTotalAmount() != null ? quotation.getTotalAmount() : 0.0))
                .serviceFrequency(ServiceFrequency.MONTHLY) // default, adjust based on your logic
                .paymentTerms(quotation.getPaymentTerms())
                .isRecurring(true)
                .contractNotes(quotation.getNotes())
                .createdBy(currentUser.getId())
                .createdAt(LocalDateTime.now())
                .lastModifiedAt(LocalDateTime.now())
                .build();

        return contractRepository.save(contract);
    }
}
