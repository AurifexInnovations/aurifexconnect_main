package com.erp.Dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PaginationResponse<T>
{
    private int pageNumber;
    private int pageSize;
    private long totalRecords;
    private int totalPages;
    private List<T> data;
}
