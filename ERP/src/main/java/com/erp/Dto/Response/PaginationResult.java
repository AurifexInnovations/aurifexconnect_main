package com.erp.Dto.Response;

import lombok.Data;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationResult<T> {
    private long count;
    private List<T> results;
}
