package com.erp.Service.Amc;

import com.erp.Model.Amc;
import com.erp.Repository.Amc.AmcRepository;
import com.erp.Repository.Invoice.InvoiceMasterRepository;
import com.erp.Service.Invoice.InvoiceOrder;
import com.erp.Service.TaskService.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AmcExecutionServiceImpl implements AmcExecutionService{


    private final AmcRepository amcRepository;
    private final AmcTxService amcTxService;


    @Override
    public void processDailyAmcs() {

        LocalDate today = LocalDate.now();

        List<Amc> amcs = amcRepository.findEligibleAmcs();

        for (Amc amc : amcs) {
            amcTxService.processSingleAmc(amc, today);
        }
    }








}
