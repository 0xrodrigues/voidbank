package com.voidbank.backend.service;

import com.voidbank.backend.model.Account;
import com.voidbank.backend.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;

    public void createAccount(Account account) {

        if (accountRepository.existsByDocument(account.getDocument())) {
            log.error("Existing account with this document");
            throw new RuntimeException("Unable to create account, document already registered");
        }
        fill(account);
        // TODO: Criar conta no banco de dados via repository

    }

    public boolean accountExists(Long nuAccount) {
        try {
            boolean exists = accountRepository.accountExists(nuAccount);
            log.debug("Account existence check for {}: {}", nuAccount, exists);
            return exists;
        } catch (Exception e) {
            log.error("Error checking if account exists: {}", nuAccount, e);
            throw new RuntimeException("Unable to check if account exists", e);
        }
    }

    public BigDecimal getBalance(Long nuAccount) {
        try {
            BigDecimal balance = accountRepository.getBalance(nuAccount);
            log.debug("Retrieved balance for account {}: {}", nuAccount, balance);
            return balance;
        } catch (Exception e) {
            log.error("Error retrieving balance for account: {}", nuAccount, e);
            throw new RuntimeException("Unable to retrieve account balance", e);
        }
    }

    private void fill(Account account) {
        Random random = new Random();
        account.setDigit(random.nextInt(999) + 1);
        account.setAgency(100);
        account.setBalance(new BigDecimal(0));
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());
    }
}