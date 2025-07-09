package com.erp.Dto.Response;

import lombok.Data;
import java.util.List;

@Data
public class ChartDataResponse {
    private List<String> labels;
    private List<Long> data;
}