package com.erp.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterRequest{

    private PaginationRequest paginationRequest;
    private Map<String, String> filterColumns;
    private Map<String, String> searchColumns;
    private Map<String, String> orderByColumns;

}
