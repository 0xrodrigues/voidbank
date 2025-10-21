package com.voidbank.backend.validator.transaction;

import com.voidbank.backend.model.Transaction;
import com.voidbank.backend.validator.TransactionValidator;

import java.math.BigDecimal;

public class ValidateAmount implements TransactionValidator {

    @Override
    public void validate(Transaction transaction) {
        if (transaction.getAmount() == null || transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
    }

}
