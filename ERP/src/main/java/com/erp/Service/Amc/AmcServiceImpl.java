package com.erp.Service.Amc;

import com.erp.Dto.Request.AmcRequestDto;
import com.erp.Dto.Request.SalesOrderRequestDto;
import com.erp.Dto.Response.AmcResponseDto;
import com.erp.Dto.Response.ResultDto;
import com.erp.Exception.ResourceNotFoundException;
import com.erp.Mapper.Amc.AmcMapper;
import com.erp.Model.*;
import com.erp.Repository.Amc.AmcRepository;
import com.erp.Repository.Branch.BranchRepository;
import com.erp.Repository.costumer.CustomerDetailsRepository;
import com.erp.Repository.salesOrder.SalesOrderRepository;
import com.erp.Security.util.UserIdentity;
import com.erp.Utility.NumberGenerator.NumberGeneratorUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AmcServiceImpl implements AmcService{

    private final SalesOrderRepository salesOrderRepository;
    private final CustomerDetailsRepository customerDetailsRepository;
    private final BranchRepository branchRepository;
    private final AmcRepository amcRepository;
    private final UserIdentity userIdentity;
    private final AmcMapper amcMapper;

    @Override
    public void createFromSalesOrder(SalesOrder salesOrder, SalesOrderRequestDto requestDto) {

        SalesOrder so = salesOrderRepository.findById(salesOrder.getSalesOrderNumber())
                .orElseThrow(()-> new ResourceNotFoundException("Sales Order not found with this id"+ salesOrder.getSalesOrderNumber()));

        CustomerDetails customerDetails = customerDetailsRepository.findById(salesOrder.getCustomerId())
                .orElseThrow(()-> new ResourceNotFoundException("Customer not found to create AMC with this id: "+salesOrder.getCustomerId()));

        Branch branch = branchRepository.findById(salesOrder.getBranch().getBranchId())
                .orElseThrow(()-> new ResourceNotFoundException("Branch not found to create AMC with this id:"+salesOrder.getBranch().getBranchId()));

        Amc amc = new Amc();

        amc.setCustomerDetails(customerDetails);
        amc.setSalesOrder(so);
        amc.setBranch(branch);

        amc.setAmcNumber(NumberGeneratorUtil.generate("AMC",amcRepository.count()+1));

        amc.setAmcType("SERVICE");
        amc.setAmcCategory(String.valueOf(salesOrder.getServiceType()));
        amc.setRecurringType(requestDto.getRecurringType());
        amc.setRecurringInterval(Math.toIntExact(requestDto.getRecurringInterval()));

        amc.setContractStartDate(requestDto.getStartDate());
        amc.setContractEndDate(requestDto.getEndDate());
        amc.setNextInvoiceDate(requestDto.getStartDate());
        amc.setLastInvoiceDate(requestDto.getStartDate());

        amc.setPerCycleAmount(salesOrder.getGrandTotal());
        amc.setTotalCompleteAmount(BigDecimal.ZERO);
        amc.setDiscountAmount(salesOrder.getDiscountPrice());

        amc.setAmcStatus("DRAFT");
        amc.setAutoGenerateInvoice(true);

        GenericUser user = userIdentity.getCurrentUser();
        amc.setCreatedBy(user.getId());
        amc.setUpdatedBy(user.getId());

        calculateCycles(amc);
        amcRepository.save(amc);

    }

    @Override
    public ResultDto<AmcResponseDto> getAllAmc() {

        List<AmcResponseDto> responseDtos = new ArrayList<>();
        for (Amc amc : amcRepository.findAll()){
            responseDtos.add(amcMapper.toAmcDto(amc));
        }
        ResultDto<AmcResponseDto> responseDtoResultDto = new ResultDto<>();
        responseDtoResultDto.setCount(responseDtos.size());
        responseDtoResultDto.setResults(responseDtos);
        return responseDtoResultDto;
    }

    @Override
    public AmcResponseDto getAmcById(Long amcId) {

        Amc amc = amcRepository.findById(amcId)
                .orElseThrow(()-> new ResourceNotFoundException("AMc not gound with this id:"+amcId));

        return amcMapper.toAmcDto(amc);
    }

    @Override
    public AmcResponseDto updateAmcById(AmcRequestDto requestDto) {

        Amc amc = amcRepository.findById(requestDto.getAmcId())
                .orElseThrow(()-> new ResourceNotFoundException("Amc not gound with this id:"+requestDto.getAmcId()));
        amc.setAmcStatus(requestDto.getAmcStatus());
        amc.setPauseReason(requestDto.getPauseReason());
        amc.setTerminationReason(requestDto.getTerminationReason());
        amcRepository.save(amc);
        return amcMapper.toAmcDto(amc);
    }

    @Override
    public ResultDto<AmcResponseDto> getAllByBranchId(Long branchId) {

        List<AmcResponseDto> responseDtos = new ArrayList<>();
        for (Amc amc : amcRepository.findAllByBranchBranchId(branchId)){
            responseDtos.add(amcMapper.toAmcDto(amc));
        }
        ResultDto<AmcResponseDto> responseDtoResultDto = new ResultDto<>();
        responseDtoResultDto.setCount(responseDtos.size());
        responseDtoResultDto.setResults(responseDtos);

        return responseDtoResultDto;
    }

    public void calculateCycles(Amc amc) {

        int intervalMonths;

        switch (amc.getRecurringType().toUpperCase()) {
            case "MONTHLY" -> intervalMonths = 1;
            case "QUARTERLY" -> intervalMonths = 3;
            case "YEARLY" -> intervalMonths = 12;
            default -> throw new IllegalArgumentException(
                    "Invalid recurring type: " + amc.getRecurringType()
            );
        }

        int servicesPerInterval = amc.getRecurringInterval(); // from frontend

        // 1. Calculate cycles per year
        int cyclesPerYear = (12 / intervalMonths) * servicesPerInterval;
        amc.setRecurringCycle(cyclesPerYear);

        // 2. Calculate total contract duration in months
        long totalMonths = ChronoUnit.MONTHS.between(amc.getContractStartDate(), amc.getContractEndDate());

        // Handle partial months: round to nearest integer
        double contractYears = totalMonths / 12.0;

        // 3. Calculate total cycles
        int totalCycles = (int) Math.round(cyclesPerYear * contractYears);
        amc.setTotalCycle(totalCycles);
        amc.setRemainCycle(totalCycles);
        amc.setCompleteCycle(0);

        BigDecimal totalAmount =
                amc.getPerCycleAmount()
                        .multiply(BigDecimal.valueOf(totalCycles));

        amc.setTotalCompleteAmount(BigDecimal.ZERO);
        amc.setContractTotalValue(totalAmount);
        amc.setTotalRemainAmount(totalAmount);

    }

}
