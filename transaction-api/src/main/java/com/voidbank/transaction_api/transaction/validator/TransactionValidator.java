package com.voidbank.transaction_api.transaction.validator;

import com.voidbank.transaction_api.transaction.model.Transaction;
import com.voidbank.transaction_api.transaction.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class TransactionValidator {

    private final AccountService accountService;

    public void validate(Transaction transaction) {
        validateAmount(transaction);
        validateAccounts(transaction);
        validateSufficientBalance(transaction);
    }

    private void validateAmount(Transaction transaction) {
        if (transaction.getAmount() == null || transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
    }

    private void validateAccounts(Transaction transaction) {
        String from = transaction.getFrom();
        String to = transaction.getTo();

        if (from == null || to == null) {
            throw new IllegalArgumentException("Both 'from' and 'to' accounts must be provided.");
        }

        if (from.equals(to)) {
            throw new IllegalArgumentException("'from' and 'to' accounts must be different.");
        }

        if (!accountService.accountExists(from)) {
            throw new IllegalArgumentException("Origin account does not exist: " + from);
        }

        if (!accountService.accountExists(to)) {
            throw new IllegalArgumentException("Destination account does not exist: " + to);
        }
    }

    private void validateSufficientBalance(Transaction transaction) {
        BigDecimal balance = accountService.getBalance(transaction.getFrom());
        if (balance.compareTo(transaction.getAmount()) < 0) {
            throw new IllegalArgumentException("Insufficient balance in the origin account.");
        }
    }
}
