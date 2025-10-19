package com.erp.Dto.Request;

import com.erp.Enum.SortCriteria;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderByRequest {
    private String sortColumn ;
    private SortCriteria sortCriteria;
}
