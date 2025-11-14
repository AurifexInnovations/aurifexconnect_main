package com.erp.Dto.Request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerMapperRequestDto {
    private Long productId;
    private Integer quantity;
}
