package com.erp.Dto.Request;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CreateSORequestDTO {

    private Long quotationId;      // quotation_id
    private Long customerId;       // customer_id
    private LocalDate orderDate;   // order_date

    private String deliveryTerms;  // delivery_terms
    private String salesNotes;     // sales_notes


    @NotNull
    private List<SalesOrderLineItemRequestDTO> lineItems;
}
