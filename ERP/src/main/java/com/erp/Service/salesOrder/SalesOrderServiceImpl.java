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

        SalesOrder order = SalesOrderMapper.toEntity(dto);
        SalesOrder savedOrder = salesOrderRepository.save(order);

        if (dto.getSoType() == SalesOrderType.PRODUCT) {
            log.info("Saving PRODUCT items");
            dto.getSalesOrderRequest().forEach(p -> {
                SaledOrderProductMapper m = new SaledOrderProductMapper();
                m.setProductId(p.getServiceOrProductId());
                m.setSaledOrderId(savedOrder.getSalesOrderNumber());
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
    public SalesOrderResponseDto update(Long id, SalesOrderRequestDto dto) {
        log.info("Updating Sales Order {}", id);
        SalesOrder order = salesOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sales order not found"));

        SalesOrder updated = SalesOrderMapper.toEntity(dto);
        updated.setSalesOrderNumber(order.getSalesOrderNumber());

        return SalesOrderMapper.toDto(salesOrderRepository.save(updated));
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
