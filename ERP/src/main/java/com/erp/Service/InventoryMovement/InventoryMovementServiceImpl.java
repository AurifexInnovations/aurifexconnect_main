package com.erp.Service.InventoryMovement;

import com.erp.Dto.Request.InventoryMovementRequest;
import com.erp.Dto.Response.InventoryMovementResponse;
import com.erp.Mapper.InventoryMovement.InventoryMovementMapper;
import com.erp.Model.*;
import com.erp.Repository.Branch.BranchRepository;
import com.erp.Repository.Inventory.InventoryRepository;
import com.erp.Repository.InventoryMovement.InventoryMovementRepository;
import com.erp.Repository.Ledger.LedgerRepository;
import com.erp.Repository.LineItems.LineItemsRepository;
import com.erp.Repository.Voucher.VoucherRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class InventoryMovementServiceImpl implements InventoryMovementService {

    private final InventoryMovementRepository movementRepository;
    private final InventoryMovementMapper movementMapper;
    private final LedgerRepository ledgerRepository;
    private final InventoryRepository inventoryRepository;
    private final VoucherRepository voucherRepository;
    private final LineItemsRepository lineItemsRepository;
    private final BranchRepository branchRepository;

    @Override
    public InventoryMovementResponse processInventoryMovement(InventoryMovementRequest request) {

        // Fetch Branch
        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch Not Found!"));

        // Fetch LineItem
        Ledger ledger = ledgerRepository.findById(request.getLedgerId())
                .orElseThrow(()-> new RuntimeException("Ledger Not Found!"));

        //Fetch Voucher
        Voucher voucher = voucherRepository.findById(request.getVoucherId())
                .orElseThrow(()-> new RuntimeException("Voucher Not Found!"));

        //Fetch LineItem
        LineItems lineItems = lineItemsRepository.findById(request.getLineItemId())
                .orElseThrow(()-> new RuntimeException("LineItem Not Found!"));

        //Fetch Inventory
        Inventory inventory = inventoryRepository.findById(request.getInventoryId())
                .orElseThrow(()-> new RuntimeException("Inventory Not Found!"));

        InventoryMovement inventoryMovement = movementMapper.mapToInventoryMovement(request);
        inventoryMovement.setInventory(inventory);
        inventoryMovement.setVoucher(voucher);
        inventoryMovement.setLedger(ledger);
        inventoryMovement.setBranch(branch);
        inventoryMovement.setLineItem(lineItems);

        movementRepository.save(inventoryMovement);
        return movementMapper.mapToInventoryMovementResponse(inventoryMovement);
    }

}