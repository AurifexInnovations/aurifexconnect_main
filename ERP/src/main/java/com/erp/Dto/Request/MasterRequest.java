package com.erp.Dto.Request;

import com.erp.Enum.TaxName;
import com.erp.Enum.TaxType;
import com.erp.Enum.VoucherType;
import com.erp.Model.Tax;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class MasterRequest {

    private String name;
    private VoucherType voucherType;
    private double amount;
    private String description;

    // Essential IDs
    private long bankAccountId;
    private long ledgerId;
    private long findMasterId;

    // Adjustments list handles invoice/bill references
    private List<AdjustmentDTO> adjustmentDTOS;
}
