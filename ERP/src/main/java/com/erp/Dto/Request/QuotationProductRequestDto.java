package com.erp.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class QuotationProductRequestDto {

    private Long productId;
    private Integer quantity;

}
