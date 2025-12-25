package com.erp.Mapper.salesOrder;


import com.erp.Dto.Request.SalesOrderRequestDto;
import com.erp.Dto.Response.SalesOrderResponseDto;
import com.erp.Model.SalesOrder;

public class SalesOrderMapper {

    public static SalesOrder toEntity(SalesOrderRequestDto dto) {
        SalesOrder order = new SalesOrder();
        order.setQuotationId(dto.getQuotationId());
        order.setDiscountPrice(dto.getDiscountPrice());
        order.setCustomerId(dto.getCustomerId());
        order.setPhoneNumber(dto.getPhoneNumber());
        order.setAlternatePhoneNumber(dto.getAlternatePhoneNumber());
      //  order.setSalesOrderDate(dto.getSalesOrderDate());
        order.setCustomerName(dto.getCustomerName());
        order.setCompanyName(dto.getCompanyName());
        order.setEmail(dto.getEmail());
        order.setAddressLine1(dto.getAddressLine1());
        order.setAddressLine2(dto.getAddressLine2());
        order.setLandmark(dto.getLandmark());
        order.setCity(dto.getCity());
        order.setState(dto.getState());
        order.setCountry(dto.getCountry());
        order.setPincode(dto.getPincode());
        order.setLocationUrl(dto.getLocationUrl());
       // order.setServiceCategory(dto.getServiceCategory());
        order.setSqft(dto.getSqft());
        order.setSoType(dto.getSoType());
        order.setStatus(dto.getStatus());
        order.setSubtotal(dto.getSubtotal());
        order.setTaxAmount(dto.getTaxAmount());
        order.setTotalAmount(dto.getTotalAmount());
        order.setGrandTotal(dto.getGrandTotal());
        order.setNotes(dto.getNotes());
        order.setServiceType(dto.getServiceType());
        return order;
    }

    public static SalesOrderResponseDto toDto(SalesOrder order) {
        SalesOrderResponseDto dto = new SalesOrderResponseDto();
        dto.setSalesOrderNumber(order.getSalesOrderNumber());
        dto.setQuotationId(order.getQuotationId());
        dto.setCustomerId(order.getCustomerId());
        dto.setBranchId(order.getBranch().getBranchId());
        dto.setPhoneNumber(order.getPhoneNumber());
        dto.setAlternatePhoneNumber(order.getAlternatePhoneNumber());
        dto.setCustomerName(order.getCustomerName());
        dto.setCompanyName(order.getCompanyName());
        dto.setEmail(order.getEmail());
        dto.setAddressLine1(order.getAddressLine1());
        dto.setAddressLine2(order.getAddressLine2());
//        dto.setSalesOrderDate(order.getSalesOrderDate());
        dto.setSoType(order.getSoType());
        dto.setStatus(order.getStatus());
        dto.setDiscountPrice(order.getDiscountPrice());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setTaxAmount(order.getTaxAmount());
        dto.setGrandTotal(order.getGrandTotal());
        dto.setServiceType(order.getServiceType());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());
        return dto;
    }
}
