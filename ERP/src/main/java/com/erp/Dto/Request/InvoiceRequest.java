package com.erp.Dto.Request;

import com.erp.Model.LineItems;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class InvoiceRequest {

    private long masterId;
    private List<LineItems> items;

}