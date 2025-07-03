package com.erp.Dto.Response;

import com.erp.Enum.VoucherType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.LocalDate;

@Getter
@Setter
public class LineItemsResponse {

    private long lineItemId;

    private String itemName;

    private double unitPrice;

    private double quantity;

    private double totalPrice;

    private VoucherType voucherType;

    private LocalDate date;

    private LastModifiedBy lastModifiedBy;
}