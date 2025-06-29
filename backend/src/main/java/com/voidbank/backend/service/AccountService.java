package com.voidbank.backend.service;

import com.voidbank.backend.exceptions.exceptions.indicator.AccountExceptionIndicator;
import com.voidbank.backend.model.Account;
import com.voidbank.backend.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

import static com.voidbank.backend.exceptions.builder.ExceptionBuilder.withIndicator;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final Random random = new Random();

    public void createAccount(Account account) {
        log.info("Starting account creation process - account {}", account);

        if (accountRepository.existsByDocument(account.getDocument())) {
            log.error("Existing account with this document");
            throw withIndicator(AccountExceptionIndicator.ACCOUNT_WITH_DOCUMENT_ALREADY_EXISTS).build();
        }
        fill(account);
        try {
            accountRepository.createAccount(account);
        } catch (Exception ex) {
            log.error("Error in account creation process - account {}", account, ex);
            throw withIndicator(AccountExceptionIndicator.ACCOUNT_NOT_FOUND).build();
        }
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
            throw withIndicator(AccountExceptionIndicator.RETRIEVING_BALANCE).build();
        }
    }

    private void fill(Account account) {
        account.setDigit(this.random.nextInt(9) + 1);
        account.setAgency(100);
        account.setBalance(new BigDecimal(0));
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());
    }
}