package com.erp.Mapper.contractMapper;

import com.erp.Dto.Request.ContractRequestDto;
import com.erp.Dto.Response.ContractResponseDto;
import com.erp.Model.Contract;


import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ContractMapper {

    public Contract toEntity(ContractRequestDto dto) {
        if (dto == null) return null;
        return Contract.builder()
                .customerId(dto.getCustomerId())
                .quotationId(dto.getQuotationId())
                .contractStatus(dto.getContractStatus())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .totalValue(dto.getTotalValue())
                .serviceFrequency(dto.getServiceFrequency())
                .paymentTerms(dto.getPaymentTerms())
                .isRecurring(dto.getIsRecurring())
                .activationDate(dto.getActivationDate())
                .renewalDate(dto.getRenewalDate())
                .contractNotes(dto.getContractNotes())
                .createdBy(dto.getCreatedBy())
                .lastModifiedBy(dto.getLastModifiedBy())
                .build();
    }

    public ContractResponseDto toResponse(Contract entity) {
        if (entity == null) return null;
        return ContractResponseDto.builder()
                .id(entity.getId())
                .customerId(entity.getCustomerId())
                .quotationId(entity.getQuotationId())
                .contractStatus(entity.getContractStatus())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .totalValue(entity.getTotalValue())
                .serviceFrequency(entity.getServiceFrequency())
                .paymentTerms(entity.getPaymentTerms())
                .isRecurring(entity.getIsRecurring())
                .activationDate(entity.getActivationDate())
                .renewalDate(entity.getRenewalDate())
                .contractNotes(entity.getContractNotes())
                .createdBy(entity.getCreatedBy())
                .createdAt(entity.getCreatedAt())
                .lastModifiedBy(entity.getLastModifiedBy())
                .lastModifiedAt(entity.getLastModifiedAt())
                .build();
    }

    public List<ContractResponseDto> toContractResponseList(List<Contract> list) {

        List<ContractResponseDto> res = new ArrayList<>();

        for (Contract contract : list) {
            res.add(toResponse(contract));
        }

        return res;

    }
}
