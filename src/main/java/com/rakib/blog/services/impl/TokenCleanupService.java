package com.rakib.blog.services.impl;

import com.rakib.blog.repository.TokenRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TokenCleanupService {

    private static final Logger log = LoggerFactory.getLogger(TokenCleanupService.class);

    @Autowired
    private TokenRepo tokenRepo;

    @Scheduled(cron = "0 0 3 * * ?")
    @Transactional
    public void cleanUpExpiredTokens() {
        log.info("Starting cleanup of logged-out tokens");
        tokenRepo.deleteAllLoggedOutTokens();
        log.info("Finished cleanup of logged-out tokens");
    }
}
