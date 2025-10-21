package com.voidbank.backend.validator.transaction;

import com.voidbank.backend.exceptions.exceptions.indicator.AccountExceptionIndicator;
import com.voidbank.backend.model.Transaction;
import com.voidbank.backend.service.AccountService;
import com.voidbank.backend.validator.TransactionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.voidbank.backend.exceptions.builder.ExceptionBuilder.withIndicator;

@Component
@RequiredArgsConstructor
public class ValidateSufficientBalance implements TransactionValidator {

    private final AccountService accountService;

    @Override
    public void validate(Transaction transaction) {
        BigDecimal balance = accountService.getBalance(transaction.getFrom());
        if (balance.compareTo(transaction.getAmount()) < 0) {
            throw withIndicator(AccountExceptionIndicator.ACCOUNT_INSUFFICIENT_BALANCE).build();
        }
    }
}
