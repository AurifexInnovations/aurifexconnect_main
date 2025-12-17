package com.erp.Service.EnhanceQuotation;

import com.erp.Dto.Request.CommanParam;
import com.erp.Dto.Request.QuotationProductRequestDto;
import com.erp.Dto.Request.QuotationRequestDto;
import com.erp.Dto.Response.QuotationResponseDto;
import com.erp.Exception.BadRequestException;
import com.erp.Model.EnhanceQuotation;
import com.erp.Model.QuotationProduc;
import com.erp.Model.QuotationService;
import com.erp.Repository.EnhanceQuotation.EnhanceQuotationRepository;
import com.erp.Repository.QuotationProductMapperRepository.QuotationProductMapperRepository;
import com.erp.Repository.QuotationServiceMapper.QuotationServiceMapperRepository;
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

    @Override
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

        // Lead / Customer
        quotation.setLeadId(dto.getLeadId());
        quotation.setCustomerId(dto.getCustomerId());

        // Personal
        quotation.setFullName(dto.getFullName());
        quotation.setCompanyName(dto.getCompanyName());
        quotation.setEmail(dto.getEmail());
        quotation.setPhone(dto.getPhone());
        quotation.setAlternatePhone(dto.getAlternatePhone());

        // Address
        quotation.setAddressLine1(dto.getAddressLine1());
        quotation.setAddressLine2(dto.getAddressLine2());
        quotation.setLandmark(dto.getLandmark());
        quotation.setCity(dto.getCity());
        quotation.setState(dto.getState());
        quotation.setCountry(dto.getCountry());
        quotation.setPincode(dto.getPincode());
        quotation.setLocationUrl(dto.getLocationUrl());

        // Category / Size
        quotation.setServiceCategory(dto.getServiceCategory());
        quotation.setSqft(dto.getSqrt() != null ? BigDecimal.valueOf(dto.getSqrt()) : BigDecimal.ZERO);

    /* =========================
       STEP 3: FINANCIALS
       ========================= */

        quotation.setSubtotal(dto.getSubtotal() != null ? dto.getSubtotal() : BigDecimal.ZERO);
        quotation.setTaxAmount(dto.getTaxAmount() != null ? dto.getTaxAmount() : BigDecimal.ZERO);
        quotation.setDiscountAmount(dto.getDiscountAmount() != null ? dto.getDiscountAmount() : BigDecimal.ZERO);

        // totalAmount = subtotal + tax
        quotation.setTotalAmount(
                quotation.getSubtotal().add(quotation.getTaxAmount())
        );

        // grandTotal = totalAmount - discount
        quotation.setGrandTotal(
                quotation.getTotalAmount().subtract(quotation.getDiscountAmount())
        );

    /* =========================
       STEP 4: CORE DETAILS
       ========================= */

        quotation.setQuotationNumber(NumberGeneratorUtil.generate("QT",enhanceQuotationRepository.count()+1));
        quotation.setQuotationDate(LocalDate.now());

        // Status
        quotation.setStatus(
                dto.getStatus() != null ? dto.getStatus() : "DRAFT"
        );

        // Lead Type
        quotation.setLeadType(hasProducts ? "PRODUCT" : "SERVICE");

        // Sent info

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
        } else {
            quotation.setRecurringType(null);
            quotation.setRecurringInterval(null);
            quotation.setRecurringCycles(null);
            quotation.setStartDate(null);
            quotation.setNextRecurringDate(null);
            quotation.setEndDate(null);
        }

        quotation.setNotes(dto.getNotes());

        EnhanceQuotation savedQuotation = enhanceQuotationRepository.save(quotation);

    /* =========================
       STEP 6: LINE ITEMS
       ========================= */

        // PRODUCTS
        if (hasProducts) {
            List<QuotationProduc> productList = new ArrayList<>();
            for (QuotationProductRequestDto p : dto.getProducts()) {
                QuotationProduc qp = new QuotationProduc();
                qp.setQuotationId(savedQuotation.getId());
                qp.setProductId(p.getProductId());
                qp.setQuantity(p.getQuantity());
                productList.add(qp);
            }
            quotationProductMapperRepository.saveAll(productList);
        }

        // SERVICES
        if (hasServices) {
            List<QuotationService> serviceList = new ArrayList<>();
            for (Long serviceId : dto.getServices()) {
                QuotationService qs = new QuotationService();
                qs.setQuotationId(savedQuotation.getId());
                qs.setServiceId(serviceId);
                serviceList.add(qs);
            }
            quotationServiceMapperRepository.saveAll(serviceList);
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
                quotation.getQuotationDate(),

                // Category / size
                quotation.getServiceCategory(),
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
