package com.erp.Dto.Response;

import com.erp.Dto.Request.BillLineItemDTO;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class BillResponseDTO {

    private Long billId;

    private Long purchaseOrderId;

    private Long vendorId;

    private LocalDate billDate;

    private LocalDate dueDate;

    private BigDecimal totalAmount;

    private String outstandingPayablesStatus;

    private String billNumber;

    private String status;

    private Long poId;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    private Boolean isActive;

    private List<BillLineItemDTO> lineItems;
}
