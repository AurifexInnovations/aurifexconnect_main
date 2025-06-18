package com.erp.Service.LineItems;

import com.erp.Dto.Request.LineItemsRequest;
import com.erp.Dto.Response.LineItemsResponse;
import com.erp.Enum.VoucherType;
import com.erp.Exception.Inventory_Exception.InsufficientStockException;
import com.erp.Exception.Ledger.LedgerNotFoundException;
import com.erp.Exception.Master.MasterNotFoundException;
import com.erp.Exception.Tax.IllegalTaxException;
import com.erp.Exception.Tax.TaxNotFoundException;
import com.erp.Exception.Voucher.VoucherNotFound;
import com.erp.Mapper.LineItems.LineItemsMapper;
import com.erp.Model.*;
import com.erp.Repository.Inventory.InventoryRepository;
import com.erp.Repository.LineItemTaxRepository;
import com.erp.Repository.LineItems.LineItemsRepository;
import com.erp.Repository.Master.MasterRepository;
import com.erp.Repository.Tax.TaxRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;

@Slf4j
@Service
@AllArgsConstructor
public class LineItemsServiceImpl implements LineItemService {

    private final LineItemsRepository lineItemsRepository;
    private final LineItemsMapper lineItemsMapper;
    private final InventoryRepository inventoryRepository;
    private final MasterRepository masterRepository;
    private final TaxRepository taxRepository;
    private final LineItemTaxRepository lineItemTaxRepository;

    @Override
    @Transactional
    public LineItemsResponse createLineItems(LineItemsRequest request) {

        Inventory inventory = inventoryRepository.findById(request.getInventoryId())
                .orElseThrow(() -> new MasterNotFoundException("Invalid Item ID: " + request.getInventoryId()));

        Master master = masterRepository.findById(request.getMasterId())
                .orElseThrow(() -> new MasterNotFoundException("Invalid Master ID: " + request.getMasterId()));

        Long masterLedgerId = master.getLedger() != null ? master.getLedger().getLedgerId() : null;
        long ledgerId = request.getLedgerId();
        double quantity = request.getQuantity();

        if (masterLedgerId == null || masterLedgerId != ledgerId) {
            throw new LedgerNotFoundException("Ledger does not match the master voucher.");
        }

        // Adjust inventory based on voucher
        switch (master.getVoucherType()) {
            case SALES    -> processSalesItem(inventory, quantity);
            case PURCHASE -> processPurchaseItem(inventory, quantity);
            default       -> throw new VoucherNotFound("Unsupported voucher type: " + master.getVoucherType());
        }

        double unitPrice = inventory.getItemCost();
        double baseAmount = unitPrice * quantity;

        LineItems lineItem = new LineItems();
        lineItem.setInventory(inventory);
        lineItem.setMaster(master);
        lineItem.setItemName(inventory.getItemName());
        lineItem.setQuantity(quantity);
        lineItem.setUnitPrice(unitPrice);
        lineItem.setVoucherType(master.getVoucherType());
        lineItem.setBaseAmount(baseAmount);

        List<LineItemTax> lineItemTaxes = new ArrayList<>();
        double totalTaxAmount = 0.0;

        if (master.getVoucherType() == VoucherType.SALES) {
            Set<Tax> taxes = new HashSet<>(Optional.ofNullable(inventory.getTaxes()).orElse(Collections.emptyList()));
            log.info("Taxes for Inventory {}: {}", inventory.getItemId(), taxes);
            if(taxes.isEmpty()){
                log.warn("No Taxes Configure1 {}", inventory.getItemName());
            }
            for (Tax tax : taxes) {
                LineItemTax lineItemTax = new LineItemTax();
                lineItemTax.setTax(tax);
                lineItemTax.setLineItems(lineItem);

                double taxAmount = switch (tax.getTaxType()) {
                    case PERCENTAGE -> baseAmount * (tax.getTaxRate().doubleValue() / 100);
                    case FIXED_AMOUNT -> tax.getTaxRate().doubleValue() * quantity;
                    default -> throw new IllegalTaxException("Unknown tax type");
                };

                lineItemTax.setTaxAmount(taxAmount);
                totalTaxAmount += taxAmount;
                lineItemTaxes.add(lineItemTax);
            }

            lineItem.setLineItemTaxes(lineItemTaxes);
            lineItem.setTotalPrice(baseAmount + totalTaxAmount);
        } else {
            lineItem.setTotalPrice(baseAmount);
        }

        lineItemsRepository.save(lineItem);

        if (!lineItemTaxes.isEmpty()) {
            // Link now that lineItem is saved and has an ID
            lineItemTaxRepository.saveAll(lineItemTaxes);
        }

        updateMasterTotal(master);

        return lineItemsMapper.mapToLineItem(lineItem);
    }

    private void processSalesItem(Inventory inventory, double quantity) {
        if (inventory.getItemQuantity() < quantity) {
            throw new InsufficientStockException("Insufficient stock for sale.");
        }
        inventory.setItemQuantity(inventory.getItemQuantity() - quantity);
        inventoryRepository.save(inventory);
    }

    private void processPurchaseItem(Inventory inventory, double quantity) {
        inventory.setItemQuantity(inventory.getItemQuantity() + quantity);
        inventoryRepository.save(inventory);
    }

    private void updateMasterTotal(Master master) {
        if (master.getVoucherType() != VoucherType.SALES) {
            throw new VoucherNotFound("Invalid Voucher Type ");
        }

        List<LineItems> lineItemsList = lineItemsRepository.findByMaster_MasterId(master.getMasterId());

        double totalBaseAmount = lineItemsList.stream()
                .mapToDouble(LineItems::getBaseAmount)
                .sum();

        double totalTaxAmount = lineItemsList.stream()
                .flatMap(li -> {
                    List<LineItemTax> taxes = li.getLineItemTaxes();
                    return taxes != null ? taxes.stream() : Stream.empty();
                })
                .mapToDouble(LineItemTax::getTaxAmount)
                .sum();

        log.info("Master {}: Base Amount = {}, Tax Amount = {}, Total Amount = {}",
                master.getMasterId(), totalBaseAmount, totalTaxAmount, totalBaseAmount + totalTaxAmount);

        master.setAmount(totalBaseAmount); // Subtotal
        master.setTaxAmount(totalTaxAmount); // All tax amounts
        master.setTotalAmount(totalBaseAmount + totalTaxAmount); // Final total
        masterRepository.save(master);
    }



    // Optional: Validate if needed in UI
    private void validateMasterTotalAmount(Master master) {
        double sumLineItems = master.getLineItems().stream()
                .mapToDouble(LineItems::getTotalPrice)
                .sum();

        if (master.getAmount() != sumLineItems) {
            throw new IllegalArgumentException("Master total does not match the sum of line items.");
        }
    }

//    @Override
//    @Transactional
//    public LineItemsResponse updateLineItem(long lineItemId, double newQuantity, long ledgerId) {
//        LineItems lineItem = lineItemsRepository.findById(lineItemId)
//                .orElseThrow(() -> new InvoiceNotFoundException("Line item not found with ID: " + lineItemId));
//
//        Master master = lineItem.getMaster();
//        Inventory inventory = lineItem.getInventory();
//
//        Long masterLedgerId = master.getLedger() != null ? master.getLedger().getLedgerId() : null;
//        if (masterLedgerId == null || masterLedgerId != ledgerId) {
//            throw new LedgerNotFoundException("Ledger does not match the master voucher.");
//        }
//
//        double oldQuantity = lineItem.getQuantity();
//        double quantityDiff = newQuantity - oldQuantity;
//
//        // Adjust inventory based on voucher type
//        switch (master.getVoucherType()) {
//            case SALES -> {
//                double newStock = inventory.getItemQuantity() - quantityDiff;
//                if (newStock < 0) {
//                    throw new InsufficientStockException("Not enough stock for update.");
//                }
//                inventory.setItemQuantity(newStock);
//            }
//            case PURCHASE -> {
//                inventory.setItemQuantity(inventory.getItemQuantity() + quantityDiff);
//            }
//            default -> throw new VoucherNotFound("Unsupported voucher type: " + master.getVoucherType());
//        }
//
//        inventoryRepository.save(inventory);
//
//        // Update line item values
//        lineItem.setQuantity(newQuantity);
//        double newTotal = newQuantity * lineItem.getUnitPrice();
//        lineItem.setTotalPrice(newTotal);
//        lineItemsRepository.save(lineItem);
//
//        updateMasterTotal(master);
//
//        return lineItemsMapper.mapToLineItem(lineItem);
//    } code was wrong have to some change


//    @Override
//    @Transactional
//    public void deleteLineItem(long lineItemId, long ledgerId) {
//        LineItems lineItem = lineItemsRepository.findById(lineItemId)
//                .orElseThrow(() -> new InvoiceNotFoundException("Line item not found with ID: " + lineItemId));
//
//        Master master = lineItem.getMaster();
//        Inventory inventory = lineItem.getInventory();
//
//        Long masterLedgerId = master.getLedger() != null ? master.getLedger().getLedgerId() : null;
//        if (masterLedgerId == null || masterLedgerId != ledgerId) {
//            throw new LedgerNotFoundException("Ledger does not match the master voucher.");
//        }
//
//        double quantity = lineItem.getQuantity();
//
//        // Restore inventory
//        switch (master.getVoucherType()) {
//            case SALES -> inventory.setItemQuantity(inventory.getItemQuantity() + quantity);
//            case PURCHASE -> {
//                double newStock = inventory.getItemQuantity() - quantity;
//                if (newStock < 0) {
//                    throw new InsufficientStockException("Cannot remove purchase. Inventory would go negative.");
//                }
//                inventory.setItemQuantity(newStock);
//            }
//            default -> throw new VoucherNotFound("Unsupported voucher type: " + master.getVoucherType());
//        }
//
//        inventoryRepository.save(inventory);
//        lineItemsRepository.delete(lineItem);
//
//        updateMasterTotal(master);
//    }


}
