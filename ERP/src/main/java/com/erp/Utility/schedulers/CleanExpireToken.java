package com.erp.Utility.schedulers;

import com.erp.Model.TokenBlackList;
import com.erp.Repository.TokenBlackList.TokenBlackListRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class CleanExpireToken {

    private static final Logger logger = LoggerFactory.getLogger(CleanExpireToken.class);
    private final TokenBlackListRepository tokenBlackListRepository;

    public CleanExpireToken(TokenBlackListRepository tokenBlackListRepository) {
        this.tokenBlackListRepository = tokenBlackListRepository;
    }

    @Scheduled(cron = "0 0/5 * * * ?") // Run every 5 minutes
    public void cleanExpiredToken() {
        logger.info("Starting cleanup of expired tokens");
        try {
            List<TokenBlackList> expiredTokens = tokenBlackListRepository.findByExpirationBefore(Instant.now().toEpochMilli());
            if (!expiredTokens.isEmpty()) {
                tokenBlackListRepository.deleteAll(expiredTokens);
                logger.info("Deleted {} expired tokens", expiredTokens.size());
            } else {
                logger.info("No expired tokens found");
            }
        } catch (Exception e) {
            logger.error("Failed to clean expired tokens", e);
            throw new RuntimeException("Failed to clean expired tokens", e);
        }
    }
}