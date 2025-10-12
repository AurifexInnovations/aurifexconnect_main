package com.erp.Dto.Request;

import lombok.Data;
import java.util.Map;

@Data
public class TicketSearchRequest {
    private Map<String, Object> filters;
    private String sortBy;
    private String sortDirection = "ASC";
    private int page = 0;
    private int size = 10;
}
