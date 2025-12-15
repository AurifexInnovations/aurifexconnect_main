package com.erp.Dto.Response;

import com.erp.Dto.Request.QuotationProductRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class QuotationResponseDto {

    private Long id;
    private String quotationNumber;
    private LocalDate quotationDate;

    private String fullName;
    private String companyName;
    private String email;
    private String phone;

    private String serviceCategory;
    private Double sqrt;

    private List<QuotationProductRequestDto> products;
    private List<Long> services;

    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal grandTotal;

    private String status;

}
