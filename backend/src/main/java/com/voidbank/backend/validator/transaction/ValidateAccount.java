package com.voidbank.backend.validator.transaction;

import com.voidbank.backend.exceptions.exceptions.indicator.AccountExceptionIndicator;
import com.voidbank.backend.model.Transaction;
import com.voidbank.backend.service.AccountService;
import com.voidbank.backend.validator.TransactionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.voidbank.backend.exceptions.builder.ExceptionBuilder.withIndicator;

@Component
@RequiredArgsConstructor
public class ValidateAccount implements TransactionValidator {

    private final AccountService accountService;

    @Override
    public void validate(Transaction transaction) {
        Long from = transaction.getFrom();
        Long to = transaction.getTo();

        if (from == null || to == null) {
            throw new IllegalArgumentException("Both 'from' and 'to' accounts must be provided.");
        }

        if (from.equals(to)) {
            throw new IllegalArgumentException("'from' and 'to' accounts must be different.");
        }

        if (!accountService.accountExists(from)) {
            throw withIndicator(AccountExceptionIndicator.ACCOUNT_NOT_FOUND).build();
        }
        if (!accountService.accountExists(to)) {
            throw withIndicator(AccountExceptionIndicator.ACCOUNT_NOT_FOUND).build();
        }
    }
}
