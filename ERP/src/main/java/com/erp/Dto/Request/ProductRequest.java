package com.erp.Dto.Request;


import com.erp.Dto.VarientDto;
import com.erp.Enum.ProductCategories;
import com.erp.Enum.ProductStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductRequest {


    private long itemId;
    private String itemName;
    private String brandName;
    private ProductCategories categories;
    private String hsnCode;
    private String skuCode;
    private String ean;
    private boolean isReturnable;
    private long taxId;
    private ProductStatus productStatus;
    private long branchId;
    private List<VarientDto> VarientList;
}
