package com.erp.Service.salesOrder;

import com.erp.Dto.Request.SalesOrderRequestDto;
import com.erp.Dto.Response.SalesOrderResponseDto;
import com.erp.Enum.SalesOrderType;
import com.erp.Mapper.salesOrder.SalesOrderMapper;
import com.erp.Model.SaledOrderProductMapper;
import com.erp.Model.SalesOrder;
import com.erp.Model.SalesOrderServiceMapper;
import com.erp.Repository.salesOrder.SaledOrderProductMapperRepository;
import com.erp.Repository.salesOrder.SalesOrderRepository;
import com.erp.Repository.salesOrder.SalesOrderServiceMapperRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SalesOrderServiceImpl implements SalesOrderService {

    private final SalesOrderRepository salesOrderRepository;
    private final SaledOrderProductMapperRepository productRepo;
    private final SalesOrderServiceMapperRepository serviceRepo;

    @Override
    public SalesOrderResponseDto create(SalesOrderRequestDto dto) {

        log.info("Creating Sales Order");

        SalesOrder order = SalesOrderMapper.toEntity(dto,null);
        SalesOrder savedOrder = salesOrderRepository.save(order);

        if (dto.getSoType() == SalesOrderType.PRODUCT) {
            log.info("Saving PRODUCT items");
            dto.getSalesOrderRequest().forEach(p -> {
                SaledOrderProductMapper m = new SaledOrderProductMapper();
                m.setProductId(p.getServiceOrProductId());
                m.setSalesOrderId(savedOrder.getSalesOrderNumber());
                m.setQuantity(p.getQuantity());
                m.setSubtotal(p.getSubtotal());
                m.setTaxAmount(p.getTaxAmount());
                m.setTotalAmount(p.getTotalAmount());
                productRepo.save(m);
            });
        } else {
            log.info("Saving SERVICE items");
            dto.getSalesOrderRequest().forEach(p -> {
                SalesOrderServiceMapper m = new SalesOrderServiceMapper();
                m.setServiceId(p.getServiceOrProductId()); // reused
                m.setSalesOrderId(savedOrder.getSalesOrderNumber());
                m.setQuantity(p.getQuantity());
                m.setSubtotal(p.getSubtotal());
                m.setTaxAmount(p.getTaxAmount());
                m.setTotalAmount(p.getTotalAmount());
                serviceRepo.save(m);
            });
        }

        return SalesOrderMapper.toDto(savedOrder);
    }

    @Override
    @Transactional
    public SalesOrderResponseDto update(Long id, SalesOrderRequestDto dto) {

        log.info("Updating Sales Order {}", id);

        SalesOrder existingOrder = salesOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sales order not found"));

        // 1️⃣ Delete old child records
        if (existingOrder.getSoType() == SalesOrderType.PRODUCT) {
            log.info("Deleting old PRODUCT items");
            productRepo.deleteBySalesOrderId(existingOrder.getSalesOrderNumber());
        } else {
            log.info("Deleting old SERVICE items");
            serviceRepo.deleteBySalesOrderId(existingOrder.getSalesOrderNumber());
        }

        // 2️⃣ Update master fields
        SalesOrder entity = SalesOrderMapper.toEntity(dto,existingOrder);

        SalesOrder updatedOrder = salesOrderRepository.save(entity);

        // 3️⃣ Insert updated child records
        if (dto.getSoType() == SalesOrderType.PRODUCT) {
            log.info("Saving updated PRODUCT items");
            dto.getSalesOrderRequest().forEach(p -> {
                SaledOrderProductMapper m = new SaledOrderProductMapper();
                m.setProductId(p.getServiceOrProductId());
                m.setSalesOrderId(updatedOrder.getSalesOrderNumber());
                m.setQuantity(p.getQuantity());
                m.setSubtotal(p.getSubtotal());
                m.setTaxAmount(p.getTaxAmount());
                m.setTotalAmount(p.getTotalAmount());
                productRepo.save(m);
            });
        } else {
            log.info("Saving updated SERVICE items");
            dto.getSalesOrderRequest().forEach(p -> {
                SalesOrderServiceMapper m = new SalesOrderServiceMapper();
                m.setServiceId(p.getServiceOrProductId());
                m.setSalesOrderId(updatedOrder.getSalesOrderNumber());
                m.setQuantity(p.getQuantity());
                m.setSubtotal(p.getSubtotal());
                m.setTaxAmount(p.getTaxAmount());
                m.setTotalAmount(p.getTotalAmount());
                serviceRepo.save(m);
            });
        }

        return SalesOrderMapper.toDto(updatedOrder);
    }

    @Override
    public SalesOrderResponseDto getById(Long id) {
        return salesOrderRepository.findById(id)
                .map(SalesOrderMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Not found"));
    }

    @Override
    public List<SalesOrderResponseDto> getAll() {
        return salesOrderRepository.findAll()
                .stream()
                .map(SalesOrderMapper::toDto)
                .toList();
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting Sales Order {}", id);
        salesOrderRepository.deleteById(id);
    }
}
