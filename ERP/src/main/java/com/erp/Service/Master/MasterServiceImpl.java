package com.erp.Service.Master;

import com.erp.Dto.Request.AdjustmentDTO;
import com.erp.Dto.Request.MasterRequest;
import com.erp.Dto.Response.MasterResponse;
import com.erp.Enum.AccountStatus;
import com.erp.Enum.ReferenceType;
import com.erp.Enum.TransactionStatus;
import com.erp.Enum.VoucherType;
import com.erp.Exception.BankAccount.BankAccountNotFoundException;
import com.erp.Exception.BankAccount.InactiveBankAccountException;
import com.erp.Exception.Ledger.LedgerNotFoundException;
import com.erp.Exception.Master.MasterNotFoundException;
import com.erp.Exception.Voucher.VoucherNotFound;
import com.erp.Mapper.Master.MasterMapper;
import com.erp.Model.BankAccount;
import com.erp.Model.Ledger;
import com.erp.Model.Master;
import com.erp.Model.Voucher;
import com.erp.Repository.BankAccount.BankAccountRepository;
import com.erp.Repository.Ledger.LedgerRepository;
import com.erp.Repository.Master.MasterRepository;
import com.erp.Repository.Voucher.VoucherRepository;
import com.erp.Service.AgainstRefMap.AgainstRefMapService;
import com.erp.Service.Voucher.VoucherService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MasterServiceImpl implements MasterService {

    private final MasterRepository masterRepository;
    private final VoucherService voucherService;
    private final MasterMapper masterMapper;
    private final LedgerRepository ledgerRepository;
    private final BankAccountRepository bankAccountRepository;
    private final VoucherRepository voucherRepository;
    private final AgainstRefMapService againstRefMapService;

    @Override
    @Transactional
    public MasterResponse createMaster(MasterRequest masterRequest) {

        Ledger ledger = ledgerRepository.findById(masterRequest.getLedgerId())
                .orElseThrow(() -> new LedgerNotFoundException("Ledger not found by this id : " + masterRequest.getLedgerId() + " , Invalid LedgerId"));

        if (masterRequest.getVoucherType() == null) {
            throw new VoucherNotFound("Voucher type must not be null or empty , Voucher Not Found ");
        }

        Voucher voucher = voucherService.generateFormattedVoucherId(masterRequest.getVoucherType());

        Master master = masterMapper.mapToMasterRequest(masterRequest);
        master.setLedger(ledger);
        master.setVoucher(voucher);

        String formattedVoucherId = voucherService.getFormattedVoucherId(voucher);
        master.setVoucherIndex(formattedVoucherId);

        masterRepository.save(master);

        switch (masterRequest.getVoucherType()) {
            case SALES -> handleInvoice(master, masterRequest);
            case PURCHASE -> handleBill(master);
            case RECEIPTS -> handleReceipt(master, masterRequest);
            case PAYMENTS -> handlePayment(master, masterRequest);
            default -> throw new VoucherNotFound("Voucher Not Found By This Voucher type : " + masterRequest.getVoucherType() + " ,Invalid Voucher Type");
        }
        return masterMapper.mapToMasterResponse(master);
    }

    @Override
    public MasterResponse findById(MasterRequest request) {
        Master master = masterRepository.findById(request.getFindMasterId())
                .orElseThrow(() -> new MasterNotFoundException("Master Not found By Id , Invalid master Id"));

        return masterMapper.mapToMasterResponse(master);
    }

    private void handleInvoice(Master invoice, MasterRequest masterRequest) {
        invoice.setReferenceType(ReferenceType.NEWREF);
        invoice.setTransactionStatus(TransactionStatus.UNPAID);

        if (masterRequest.getAdjustmentDTOS() == null || masterRequest.getAdjustmentDTOS().isEmpty()) return;

        double totalAdjusted = 0;

        for (AdjustmentDTO adjustment : masterRequest.getAdjustmentDTOS()) {
            Master advanceReceipt = validateReferenceVoucher(adjustment.getInvoiceAndBillId(), VoucherType.RECEIPTS);

            double adjAmt = adjustment.getAdjustAmount();
            totalAdjusted += adjAmt;

            // Update advance mapping (adjustment of advance to this invoice)
            againstRefMapService.againRefMap(advanceReceipt, invoice.getLedger(), ReferenceType.AGAINREF, adjAmt);

            double alreadyAdjusted = masterRepository.findByReferenceMaster(advanceReceipt)
                    .stream().mapToDouble(Master::getAmount).sum();

            double totalUsed = alreadyAdjusted + adjAmt;
            advanceReceipt.setTransactionStatus(getUpdatedStatus(advanceReceipt.getAmount(), totalUsed));
            masterRepository.save(advanceReceipt);
        }

        double remaining = invoice.getAmount() - totalAdjusted;

        if (remaining == 0) {
            invoice.setTransactionStatus(TransactionStatus.PAID);
        } else if (totalAdjusted > 0) {
            invoice.setTransactionStatus(TransactionStatus.PARTIALLY_PAID);
        } else {
            invoice.setTransactionStatus(TransactionStatus.UNPAID);
        }
    }


    private void handleBill(Master master) {
        master.setTransactionStatus(TransactionStatus.UNPAID);
        master.setReferenceType(ReferenceType.NEWREF);
    }

    @Transactional
    private void handleReceipt(Master master, MasterRequest masterRequest) {
        BankAccount bankAccount = validateBankAccount(masterRequest.getBankAccountId());

        double totalAdjusted = 0;

        if (masterRequest.getAdjustmentDTOS() != null && !masterRequest.getAdjustmentDTOS().isEmpty()) {
            for (AdjustmentDTO adjustment : masterRequest.getAdjustmentDTOS()) {
                Master invoice = validateReferenceVoucher(adjustment.getInvoiceAndBillId(), VoucherType.SALES);

                double adjAmt = adjustment.getAdjustAmount();
                totalAdjusted += adjAmt;

                againstRefMapService.againRefMap(master, master.getLedger(), ReferenceType.AGAINREF, adjAmt);

                double alreadyAdjusted = masterRepository.findByReferenceMaster(invoice)
                        .stream().mapToDouble(Master::getAmount).sum();

                double newTotal = alreadyAdjusted + adjAmt;
                invoice.setTransactionStatus(getUpdatedStatus(invoice.getAmount(), newTotal));
                masterRepository.save(invoice);
            }

            double remaining = master.getAmount() - totalAdjusted;
            if (remaining > 0) {
                master.setReferenceType(ReferenceType.ADVANCEREF);
            } else {
                master.setReferenceType(ReferenceType.AGAINREF);
            }

        } else {
            master.setReferenceType(ReferenceType.ADVANCEREF);
        }

        bankAccount.setCurrentBalance(bankAccount.getCurrentBalance() + master.getAmount());
        bankAccountRepository.save(bankAccount);

        master.setBankAccount(bankAccount);
    }

    @Transactional
    private void handlePayment(Master master, MasterRequest masterRequest) {
        BankAccount bankAccount = validateBankAccount(masterRequest.getBankAccountId());

        double totalAdjusted = 0;

        if (masterRequest.getAdjustmentDTOS() != null && !masterRequest.getAdjustmentDTOS().isEmpty()) {
            for (AdjustmentDTO adjustment : masterRequest.getAdjustmentDTOS()) {
                Master bill = validateReferenceVoucher(adjustment.getInvoiceAndBillId(), VoucherType.PURCHASE);

                double adjAmt = adjustment.getAdjustAmount();
                totalAdjusted += adjAmt;

                againstRefMapService.againRefMap(master, master.getLedger(), ReferenceType.AGAINREF, adjAmt);

                double alreadyAdjusted = masterRepository.findByReferenceMaster(bill)
                        .stream().mapToDouble(Master::getAmount).sum();

                double newTotal = alreadyAdjusted + adjAmt;
                bill.setTransactionStatus(getUpdatedStatus(bill.getAmount(), newTotal));
                masterRepository.save(bill);
            }

            double remaining = master.getAmount() - totalAdjusted;
            if (remaining > 0) {
                master.setReferenceType(ReferenceType.ADVANCEREF);
            } else {
                master.setReferenceType(ReferenceType.AGAINREF);
            }

        } else {
            master.setReferenceType(ReferenceType.ADVANCEREF);
        }

        bankAccount.setCurrentBalance(bankAccount.getCurrentBalance() - master.getAmount());
        bankAccountRepository.save(bankAccount);

        master.setBankAccount(bankAccount);
    }

    private BankAccount validateBankAccount(Long id) {
        BankAccount bankAccount = bankAccountRepository.findById(id)
                .orElseThrow(() -> new BankAccountNotFoundException("Bank account not found, Invalid bank Account Id"));

        if (bankAccount.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new InactiveBankAccountException("Bank account is INACTIVE. Please contact admin.");
        }
        return bankAccount;
    }

    private Master validateReferenceVoucher(Long id, VoucherType expectedType) {
        Master master = masterRepository.findById(id)
                .orElseThrow(() -> new MasterNotFoundException("Reference voucher not found with id: " + id));

        if (master.getVoucherType() != expectedType) {
            throw new VoucherNotFound("Provided related ID is not of type " + expectedType);
        }
        return master;
    }

    private TransactionStatus getUpdatedStatus(double totalAmount, double paidAmount) {
        return Double.compare(paidAmount, totalAmount) == 0
                ? TransactionStatus.PAID
                : TransactionStatus.PARTIALLY_PAID;
    }
}

//    @Override
//    public MasterResponse deleteMaster(Long masterId) {
//        Master master = masterRepository.findById(masterId)
//                .orElseThrow(() -> new MasterNotFoundException("Master not found with id: " + masterId));
//
//        VoucherType voucherType = master.getVoucherType();
//
//        switch (voucherType) {
//            case RECEIPTS -> {
//                BankAccount bankAccount = master.getBankAccount();
//                if (bankAccount != null) {
//                    bankAccount.setCurrentBalance(bankAccount.getCurrentBalance() - master.getAmount());
//                    bankAccountRepository.save(bankAccount);
//                }
//
//                if (master.getReferenceType() == ReferenceType.NEWREF || master.getReferenceType() == ReferenceType.ADVANCEREF) {
//                    Master invoice = masterRepository.findById(master.getMasterId())
//                            .orElseThrow(() -> new InvoiceNotFoundException("Linked invoice not found"));
//
//                    List<Master> relatedReceipts = masterRepository.findByVoucherTypeAndMasterId(VoucherType.SALES, invoice.getMasterId());
//                    double totalPaid = relatedReceipts.stream()
//                            .filter(p -> !Objects.equals(p.getMasterId(), masterId))
//                            .mapToDouble(Master::getAmount)
//                            .sum();
//
//                    if (Double.compare(totalPaid, 0.0) == 0) {
//                        invoice.setTransactionStatus(TransactionStatus.UNPAID);
//                    } else if (Double.compare(totalPaid, invoice.getAmount()) == 0) {
//                        invoice.setTransactionStatus(TransactionStatus.PAID);
//                    } else {
//                        invoice.setTransactionStatus(TransactionStatus.PARTIALLY_PAID);
//                    }
//                    masterRepository.save(invoice);
//                }
//            }
//
//            case PAYMENTS -> {
//                BankAccount bankAccount = master.getBankAccount();
//                if (bankAccount != null) {
//                    bankAccount.setCurrentBalance(bankAccount.getCurrentBalance() + master.getAmount());
//                    bankAccountRepository.save(bankAccount);
//                }
//
//                if (master.getReferenceType() == ReferenceType.AGAINREF || master.getReferenceType() == ReferenceType.ADVANCEREF) {
//                    Master bill = masterRepository.findById(master.getMasterId())
//                            .orElseThrow(() -> new InvoiceNotFoundException("Linked bill not found"));
//
//                    List<Master> relatedPayments = masterRepository.findByVoucherTypeAndMasterId(VoucherType.PURCHASE, bill.getMasterId());
//                    double totalPaid = relatedPayments.stream()
//                            .filter(p -> !Objects.equals(p.getMasterId(), masterId))
//                            .mapToDouble(Master::getAmount)
//                            .sum();
//
//                    if (Double.compare(totalPaid, 0.0) == 0) {
//                        bill.setTransactionStatus(TransactionStatus.UNPAID);
//                    } else if (Double.compare(totalPaid, bill.getAmount()) == 0) {
//                        bill.setTransactionStatus(TransactionStatus.PAID);
//                    } else {
//                        bill.setTransactionStatus(TransactionStatus.PARTIALLY_PAID);
//                    }
//                    masterRepository.save(bill);
//                }
//            }
//
//            case SALES, PURCHASE -> {
//                // Nothing extra needed unless you handle cascading deletes or specific logic.
//            }
//
//            default -> throw new VoucherNotFound("Invalid voucher type: " + voucherType);
//        }
//
//        masterRepository.deleteById(masterId);
//        return masterMapper.mapToMasterResponse(master);
//    }
