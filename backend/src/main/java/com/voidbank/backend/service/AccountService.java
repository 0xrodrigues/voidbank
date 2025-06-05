package com.voidbank.backend.service;

import com.voidbank.backend.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;

    public boolean accountExists(String nuAccount) {
        try {
            boolean exists = accountRepository.accountExists(nuAccount);
            log.debug("Account existence check for {}: {}", nuAccount, exists);
            return exists;
        } catch (Exception e) {
            log.error("Error checking if account exists: {}", nuAccount, e);
            return false;
        }
    }

    public BigDecimal getBalance(String nuAccount) {
        try {
            BigDecimal balance = accountRepository.getBalance(nuAccount);
            log.debug("Retrieved balance for account {}: {}", nuAccount, balance);
            return balance;
        } catch (Exception e) {
            log.error("Error retrieving balance for account: {}", nuAccount, e);
            throw new RuntimeException("Unable to retrieve account balance", e);
        }
    }
}