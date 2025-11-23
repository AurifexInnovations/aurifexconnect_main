package com.erp.Dto.Request;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class UpdateBillRequestDTO {

    private Long billId;
    private Long vendorId;
    private LocalDate billDate;
    private LocalDate dueDate;
    private String status;
    private BigDecimal totalAmount;
    private String billNumber;
    private String outstandingPayablesStatus;
    private List<BillLineItemDTO> lineItems;
}
