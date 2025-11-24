package com.erp.Dto.Response;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class SalesOrderResponseDTO {

    private Long soId;
    private Long customerId;
    private Long quoteId;

    private String status;
    private BigDecimal totalValue;

    private LocalDate orderDate;
    private String deliveryTerms;
    private String salesNotes;

    private Long createdBy;
    private LocalDateTime updatedAt;

    private List<SalesOrderLineItemResponseDTO> lineItems;
}

