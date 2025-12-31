package com.erp.Service.Amc;

import com.erp.Model.Amc;
import com.erp.Repository.Amc.AmcRepository;
import com.erp.Service.Invoice.InvoiceOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AmcTxService {

    private final InvoiceOrder invoiceService;
    private final AmcRepository amcRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processSingleAmc(Amc amc, LocalDate today) {

        if (isIgnored(amc)) return;

        if (isFirstExecution(amc, today)) {
            amc.setAmcStatus("ACTIVE");
            executeAmc(amc, today);
            return;
        }

        if (isNextExecution(amc, today)) {
            executeAmc(amc, today);
        }
    }
    private void executeAmc(Amc amc, LocalDate today) {

        // 1️⃣ Create Invoice
        if (amc.getAutoGenerateInvoice()) {
            invoiceService.createFromAmc(amc);
        }

        // 2️⃣ Create Task
//        if (amc.getAutoGenerateTask()) {
//            taskService.createFromAmc(amc);
//        }

        // 3️⃣ Update cycles
        amc.setCompleteCycle(amc.getCompleteCycle() + 1);
        amc.setRemainCycle(amc.getRemainCycle() - 1);

        /*
         Amount update on payment paid
         */

        // 4️⃣ Update amounts based on
//        BigDecimal perCycle = amc.getPerCycleAmount();
//
//        amc.setTotalCompleteAmount(
//                amc.getTotalCompleteAmount().add(perCycle)
//        );
//
//        amc.setTotalRemainAmount(
//                amc.getTotalRemainAmount().subtract(perCycle)
//        );

        // 5️⃣ Update dates
        amc.setLastInvoiceDate(today);

        if (amc.getRemainCycle() > 0) {
            amc.setNextInvoiceDate(calculateNextDate(amc, today));
        }

        // 6️⃣ Completion check
        if (amc.getRemainCycle() == 0) {
            amc.setAmcStatus("COMPLETED");
        }

        amc.setUpdatedAt(LocalDateTime.now());
        amcRepository.save(amc);
    }

    private LocalDate calculateNextDate(Amc amc, LocalDate baseDate) {

        return switch (amc.getRecurringType().toUpperCase()) {
            case "MONTHLY" -> baseDate.plusMonths(1);
            case "QUARTERLY" -> baseDate.plusMonths(3);
            case "YEARLY" -> baseDate.plusYears(1);
            default -> throw new IllegalArgumentException("Invalid recurring type");
        };
    }

    private boolean isIgnored(Amc amc) {
        return "PAUSED".equals(amc.getAmcStatus())
                || "TERMINATED".equals(amc.getAmcStatus())
                || "COMPLETED".equals(amc.getAmcStatus());
    }

    private boolean isFirstExecution(Amc amc, LocalDate today) {
        return "DRAFT".equals(amc.getAmcStatus())
                && today.equals(amc.getContractStartDate());
    }

    private boolean isNextExecution(Amc amc, LocalDate today) {
        return "ACTIVE".equals(amc.getAmcStatus())
                && today.equals(amc.getNextInvoiceDate());
    }

}
