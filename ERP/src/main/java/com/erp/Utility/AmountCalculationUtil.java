package com.erp.Utility;

import com.erp.Dto.Request.*;
import com.erp.Enum.ServiceCategory;
import com.erp.Exception.Inventory_Exception.InventoryNotFoundException;
import com.erp.Exception.Service_Exception.ServiceNotFoundException;
import com.erp.Model.InventoryV2;
import com.erp.Model.Service;
import com.erp.Repository.Inventory.InventoryRepositoryV2;
import com.erp.Repository.Service.ServiceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@AllArgsConstructor
public class AmountCalculationUtil {

    private final ServiceRepository serviceRepository;
    private final InventoryRepositoryV2 inventoryRepositoryV2;

    public CalculationVar serviceAmoCal(List<CommanParam> list, Double sqft, Double disAmount, ServiceCategory category) {

        BigDecimal total = BigDecimal.ZERO;
        BigDecimal taxAmount = BigDecimal.ZERO;
        BigDecimal subTotal;
        BigDecimal grandTotal;

        for (CommanParam l : list) {

            Service s = serviceRepository.findById(l.getId())
                    .orElseThrow(() ->
                            new ServiceNotFoundException(
                                    "Service not Found with id: " + l.getId()));

            // choose price based on category
            BigDecimal price;
            if (category == ServiceCategory.RESIDENTIAL) {
                price = s.getResidentialPrice();
            } else {
                price = s.getCommercialPrice();
            }

            // total += (sqft / 100) * price
            BigDecimal serviceAmount =
                    BigDecimal.valueOf(sqft)
                            .multiply(price)
                            .divide(BigDecimal.valueOf(100));

            total = total.add(serviceAmount);
        }

        // subtotal = total + tax (tax is 0)
        subTotal = total.add(taxAmount);

        // discount null safety
        BigDecimal discount =
                disAmount == null ? BigDecimal.ZERO : BigDecimal.valueOf(disAmount);

        // grand total = subtotal - discount
        grandTotal = subTotal.subtract(discount);

        return new CalculationVar(
                total,
                taxAmount,
                subTotal,
                grandTotal
        );
    }
    public CalculationVar getCounting(
            List<QuotationProductRequestDto> items,
            Double discountAmount
    ) {

        BigDecimal total = BigDecimal.ZERO;
        BigDecimal taxAmount = BigDecimal.ZERO;

        for (QuotationProductRequestDto dto : items) {

            InventoryV2 inventory = inventoryRepositoryV2.findById(dto.getProductId())
                    .orElseThrow(() ->
                            new InventoryNotFoundException("Inventory not found"));

            BigDecimal price = BigDecimal.valueOf(inventory.getSellingPrice());
            BigDecimal quantity = new BigDecimal(dto.getQuantity());

            BigDecimal itemTotal = price.multiply(quantity);
            total = total.add(itemTotal);

            BigDecimal taxRate = inventory.getTax()
                    .getTaxRate()
                    .divide(BigDecimal.valueOf(100));

            taxAmount = taxAmount.add(itemTotal.multiply(taxRate));
        }

        BigDecimal subTotal = total.add(taxAmount);
        BigDecimal discount =
                discountAmount == null ? BigDecimal.ZERO : BigDecimal.valueOf(discountAmount);

        return new CalculationVar(
                total,
                taxAmount,
                subTotal,
                subTotal.subtract(discount)
        );
    }

}


