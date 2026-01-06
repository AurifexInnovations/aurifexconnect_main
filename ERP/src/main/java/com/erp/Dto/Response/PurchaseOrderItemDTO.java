package com.erp.Dto.Response;

import lombok.Data;

@Data
public class PurchaseOrderItemDTO {

    private Long inventory_id;
    private String item_name;
    private Double qty;
    private String measurement_unit;
    private Double measurement_value;
    private Double price;
    private Double discount;
    private Double tax_percent;
    private Double line_total;
}
