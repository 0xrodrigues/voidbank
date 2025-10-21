package com.voidbank.backend.validator;

import com.voidbank.backend.model.Transaction;

public interface TransactionValidator {
    void validate(Transaction transaction);
}
