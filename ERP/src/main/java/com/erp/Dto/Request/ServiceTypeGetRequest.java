package com.erp.Dto.Request;

import com.erp.Enum.ServiceCategory;
import com.erp.Enum.ServiceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ServiceTypeGetRequest {

    private  PaginationRequest paginationRequest;
    private  String serviceName ;
    private ServiceStatus serviceStatus;
    private ServiceCategory serviceCategory;
    private Double servicePrice;
    private DateRequest dateRequest;
    private  OrderByRequest orderByRequest;
    Map<String, String> searchFilters;

}
