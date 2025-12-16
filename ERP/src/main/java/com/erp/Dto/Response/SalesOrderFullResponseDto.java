package com.erp.Dto.Response;

import com.erp.Projection.SalesOrderProductProjection;
import com.erp.Projection.SalesOrderProjection;
import com.erp.Projection.SalesOrderServiceProjection;
import lombok.Data;

import java.util.List;

@Data
public class SalesOrderFullResponseDto {

    private SalesOrderProjection order;

    private List<SalesOrderProductProjection> products;

    private List<SalesOrderServiceProjection> services;
}
