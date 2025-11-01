package com.erp.Service.Shipment;


import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Request.ShipmentDetailsRequestDto;

import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.ShipmentResponseDto;
import com.erp.Model.ShipmentDetails;


import java.util.List;


public interface ShipmentService {

    ShipmentResponseDto createShipment(ShipmentDetailsRequestDto requestDto);

    ShipmentResponseDto getShipmentById(Long id);

     List<ShipmentDetails> getAllShipmentsWithPagination(int limit, int offset);

    String deleteShipmentById(Long id);

    ResultDto<ShipmentDetails> getAllShipments(FilterRequest filterRequest);
}
