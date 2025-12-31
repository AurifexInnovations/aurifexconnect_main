package com.erp.Scheduler.Amc;

import com.erp.Service.Amc.AmcExecutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AmcScheduler {

    private final AmcExecutionService amcExecutionService;


    @Scheduled(cron = "0 0 1 * * *")
    public void runDailyAmcJob() {
        amcExecutionService.processDailyAmcs();
    }

}
