package com.erp.Dto.Response;

import com.erp.Dto.Request.CustomerMapperRequestDto;
import lombok.*;
        import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponseDtos {
    private Long id;
    private String customerName;
    private String companyName;
    private String email;
    private String phone;
    private String city;
    private String country;
    private String tags;
    private String customerStatus;
    private LocalDate joinedDate;
    private List<CustomerMapperRequestDto> products;
}