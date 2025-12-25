package com.erp.Service.EnhanceQuotation;

import com.erp.Dto.Request.*;
import com.erp.Dto.Response.QuotationResponseDto;
import com.erp.Enum.ServiceCategory;
import com.erp.Exception.BadRequestException;
import com.erp.Exception.ResourceNotFoundException;
import com.erp.Model.*;
import com.erp.Repository.Branch.BranchRepository;
import com.erp.Repository.EnhanceQuotation.EnhanceQuotationRepository;
import com.erp.Repository.Lead.LeadRepositorys;
import com.erp.Repository.QuotationProductMapperRepository.QuotationProductMapperRepository;
import com.erp.Repository.QuotationServiceMapper.QuotationServiceMapperRepository;
import com.erp.Repository.costumer.CustomerDetailsRepository;
import com.erp.Utility.AmountCalculationUtil;
import com.erp.Utility.NumberGenerator.NumberGeneratorUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EnhanceQuotationServiceImpl implements EnhanceQuotationService {

    final private EnhanceQuotationRepository enhanceQuotationRepository;
    final private QuotationProductMapperRepository quotationProductMapperRepository;
    final private QuotationServiceMapperRepository quotationServiceMapperRepository;
    private final AmountCalculationUtil amountCalculationUtil;
    private final LeadRepositorys leadRepository;
    private final CustomerDetailsRepository customerDetailsRepository;
    private final BranchRepository branchRepository;


    public QuotationResponseDto addQuotation(QuotationRequestDto dto) {

    /* =========================
       STEP 1: VALIDATION
       ========================= */
        if (dto.getLeadId() == null && dto.getCustomerId() == null) {
            throw new BadRequestException("Either leadId or customerId is required");
        }



        boolean hasProducts = dto.getProducts() != null && !dto.getProducts().isEmpty();
        boolean hasServices = dto.getServices() != null && !dto.getServices().isEmpty();

        if (hasProducts && hasServices) {
            throw new BadRequestException("Quotation cannot contain both products and services");
        }

        if (!hasProducts && !hasServices) {
            throw new BadRequestException("Quotation must contain either products or services");
        }

    /* =========================
       STEP 2: CREATE QUOTATION
       ========================= */
        EnhanceQuotation quotation = new EnhanceQuotation();

        quotation.setLeadId(dto.getLeadId());
        quotation.setCustomerId(dto.getCustomerId());

        quotation.setFullName(dto.getFullName());
        quotation.setCompanyName(dto.getCompanyName());
        quotation.setEmail(dto.getEmail());
        quotation.setPhone(dto.getPhone());
        quotation.setAlternatePhone(dto.getAlternatePhone());

        quotation.setAddressLine1(dto.getAddressLine1());
        quotation.setAddressLine2(dto.getAddressLine2());
        quotation.setLandmark(dto.getLandmark());
        quotation.setCity(dto.getCity());
        quotation.setState(dto.getState());
        quotation.setCountry(dto.getCountry());
        quotation.setPincode(dto.getPincode());
        quotation.setLocationUrl(dto.getLocationUrl());

        quotation.setServiceType(dto.getServiceType());
        quotation.setSqft(
                dto.getSqrt() != null ? BigDecimal.valueOf(dto.getSqrt()) : BigDecimal.ZERO
        );

        if(dto.getLeadId() != null){
            Leads leads = leadRepository.findById(dto.getLeadId())
                            .orElseThrow(()-> new ResourceNotFoundException("Lead not found with this id : "+dto.getLeadId()));
            leads.setTotalQuotation(leadRepository.count()+1);
            leadRepository.save(leads);
        }else {
            CustomerDetails customerDetails = customerDetailsRepository.findById(dto.getCustomerId())
                    .orElseThrow(()-> new ResourceNotFoundException("Customer Not Found With this Id : "+ dto.getCustomerId()));
            customerDetails.setTotalQuotation(customerDetailsRepository.count()+1);
            customerDetailsRepository.save(customerDetails);
        }

        if (dto.getBranchId() != null){
           Branch branch =  branchRepository.findById(dto.getBranchId())
                    .orElseThrow(()-> new ResourceNotFoundException("Branch not Found with this Branch Id : "+dto.getBranchId()));
            quotation.setBranch(branch);

        }


    /* =========================
       STEP 3: FINANCIALS
       ========================= */
//        BigDecimal discount =
//                dto.getDiscountAmount() != null ? dto.getDiscountAmount() : BigDecimal.ZERO;
//
//        CalculationVar calc;
//
//        // SERVICE QUOTATION
//        if (hasServices) {
//
//            List<CommanParam> serviceParams =
//                    dto.getServices().stream()
//                            .map(CommanParam::new)
//                            .toList();
//
//            calc = amountCalculationUtil.serviceAmoCal(
//                    serviceParams,
//                    dto.getSqrt(),
//                    discount.doubleValue(),   // util expects Double (can be improved later)
//                    ServiceCategory.valueOf(dto.getServiceCategory())  // already enum
//            );
//        }
//        // PRODUCT QUOTATION
//        else {
//            calc = amountCalculationUtil.getCounting(
//                    dto.getProducts(),
//                    discount.doubleValue()
//            );
//        }
//
//        // ✅ Single source of truth for financials
//        quotation.setSubtotal(calc.getTotal());
//        quotation.setTaxAmount(calc.getTaxAmount());
//        quotation.setTotalAmount(calc.getSubTotal());
//        quotation.setDiscountAmount(discount);
//        quotation.setGrandTotal(calc.getGrandTotal());

        quotation.setSubtotal(dto.getSubtotal());
        quotation.setTaxAmount(dto.getTaxAmount());
        quotation.setTotalAmount(dto.getTotalAmount());
        quotation.setDiscountAmount(dto.getDiscountAmount());
        quotation.setGrandTotal(dto.getGrandTotal());


    /* =========================
       STEP 4: CORE DETAILS
       ========================= */
        quotation.setQuotationNumber(
                NumberGeneratorUtil.generate("QT", enhanceQuotationRepository.count() + 1)
        );
       // quotation.setQuotationDate(LocalDate.now());
       quotation.setQuotationType(dto.getQuotationType());
       quotation.setStatus(dto.getStatus());

        quotation.setSentDate(LocalDateTime.now());
        quotation.setSentVia(dto.getSentVia());

    /* =========================
       STEP 5: RECURRING
       ========================= */
        quotation.setIsRecurring(dto.getIsRecurring());

        if (Boolean.TRUE.equals(dto.getIsRecurring())) {
            quotation.setRecurringType(dto.getRecurringType());
            quotation.setRecurringInterval(dto.getRecurringInterval());
            quotation.setRecurringCycles(dto.getRecurringCycles());
            quotation.setStartDate(dto.getStartDate());
            quotation.setNextRecurringDate(dto.getNextRecurringDate());
            quotation.setEndDate(dto.getEndDate());
        }

        quotation.setNotes(dto.getNotes());

        EnhanceQuotation savedQuotation = enhanceQuotationRepository.save(quotation);

    /* =========================
       STEP 6: LINE ITEMS
       ========================= */
        if (hasProducts) {
            List<QuotationProduc> products = dto.getProducts().stream()
                    .map(p -> {
                        QuotationProduc qp = new QuotationProduc();
                        qp.setQuotationId(savedQuotation.getId());
                        qp.setProductId(p.getProductId());
                        qp.setQuantity(p.getQuantity());
                        return qp;
                    })
                    .toList();

            quotationProductMapperRepository.saveAll(products);
        }

        if (hasServices) {
            List<QuotationService> services = dto.getServices().stream()
                    .map(id -> {
                        QuotationService qs = new QuotationService();
                        qs.setQuotationId(savedQuotation.getId());
                        qs.setServiceId(id);
                        return qs;
                    })
                    .toList();

            quotationServiceMapperRepository.saveAll(services);
        }

    /* =========================
       STEP 7: RESPONSE
       ========================= */
        return toResponseDto(savedQuotation);
    }




    private QuotationResponseDto toResponseDto(EnhanceQuotation quotation) {

        // Fetch product mappings only if they exist
        List<QuotationProductRequestDto> productDtos = new ArrayList<>();
        List<QuotationService> serviceMappers = new ArrayList<>();
        List<Long> serviceIds = new ArrayList<>();

        if (quotationProductMapperRepository.findByQuotationId(quotation.getId()) != null &&
                !quotationProductMapperRepository.findByQuotationId(quotation.getId()).isEmpty()) {

            List<QuotationProduc> productMappers =
                    quotationProductMapperRepository.findByQuotationId(quotation.getId());

            productDtos = productMappers.stream()
                    .map(p -> new QuotationProductRequestDto(
                            p.getProductId(),
                            p.getQuantity()
                    ))
                    .toList();

        } else if (quotationServiceMapperRepository.findByQuotationId(quotation.getId()) != null &&
                !quotationServiceMapperRepository.findByQuotationId(quotation.getId()).isEmpty()) {

            serviceMappers =
                    quotationServiceMapperRepository.findByQuotationId(quotation.getId());

            serviceIds = serviceMappers.stream()
                    .map(QuotationService::getServiceId)
                    .toList();
        }

        return new QuotationResponseDto(
                // IDs
                quotation.getId(),
                quotation.getLeadId(),
                quotation.getCustomerId(),
                quotation.getBranch().getBranchId(),

                // Personal / Company
                quotation.getFullName(),
                quotation.getCompanyName(),
                quotation.getEmail(),
                quotation.getPhone(),
                quotation.getAlternatePhone(),

                // Address
                quotation.getAddressLine1(),
                quotation.getAddressLine2(),
                quotation.getLandmark(),
                quotation.getCity(),
                quotation.getState(),
                quotation.getCountry(),
                quotation.getPincode(),
                quotation.getLocationUrl(),

                // Quotation core
                quotation.getQuotationNumber(),
               // quotation.getQuotationDate(),

                // Category / size
                quotation.getServiceType(),
                quotation.getSqft() != null ? quotation.getSqft().doubleValue() : null,

                // Line items
                productDtos,
                serviceIds,

                // Financials
                quotation.getSubtotal(),
                quotation.getTaxAmount(),
                quotation.getTotalAmount(),
                quotation.getDiscountAmount(),
                quotation.getGrandTotal(),

                // Status
                quotation.getQuotationType(),
                quotation.getStatus(),


                // Sent info
                quotation.getSentDate(),
                quotation.getSentVia(),

                // Recurring
                quotation.getIsRecurring(),
                quotation.getRecurringType(),
                quotation.getRecurringInterval(),
                quotation.getRecurringCycles(),
                quotation.getStartDate(),
                quotation.getNextRecurringDate(),
                quotation.getEndDate(),

                // Notes
                quotation.getNotes(),

                // Audit
                quotation.getCreatedAt(),
                quotation.getUpdatedAt()
        );
    }

    @Override
    public QuotationResponseDto quotationGetById(CommanParam param) {

        EnhanceQuotation quotation = enhanceQuotationRepository.findById(param.getId())
                .orElseThrow(() -> new BadRequestException("Quotation not found with id: " + param.getId()));
        return toResponseDto(quotation);
    }

    @Override
    public List<QuotationResponseDto> quotationGetAll() {

        List<EnhanceQuotation> quotations = enhanceQuotationRepository.findAll();

        return quotations.stream()
                .map(this::toResponseDto)
                .toList();
    }


}
