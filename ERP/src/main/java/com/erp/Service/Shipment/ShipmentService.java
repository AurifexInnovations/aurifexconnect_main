package com.erp.Service.Shipment;


import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Request.ShipmentDetailsRequestDto;

import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.ShipmentDetailsResponseDTO;
import com.erp.Dto.Response.ShipmentResponseDto;
import com.erp.Model.ShipmentDetails;


import java.util.List;


public interface ShipmentService {

    ShipmentDetailsResponseDTO createShipment(ShipmentDetailsRequestDto requestDto);

    ShipmentDetailsResponseDTO getShipmentById(Long id);

    ResultDto<ShipmentDetailsResponseDTO> getAllShipmentsWithPagination();

    String deleteShipmentById(Long id);

    ResultDto<ShipmentDetailsResponseDTO> getAllShipments(FilterRequest filterRequest);
}
