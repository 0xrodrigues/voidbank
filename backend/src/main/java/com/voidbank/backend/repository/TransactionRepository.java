package com.voidbank.backend.repository;

import com.voidbank.backend.model.Transaction;
import com.voidbank.backend.model.enums.TransactionStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
@Slf4j
@RequiredArgsConstructor
public class TransactionRepository {

    private static final String INSERT_QUERY = """
                INSERT INTO transactions (
                    nu_transaction,
                    token,
                    from_nu_account,
                    to_nu_account,
                    amount,
                    rate,
                    comments,
                    status,
                    created_at,
                    updated_at,
                    transaction_type
                ) VALUES (
                    :nuTransaction,
                    :token,
                    :from,
                    :to,
                    :amount,
                    :rate,
                    :comments,
                    :status,
                    :createdAt,
                    :updatedAt,
                    :transaction_type
                )
            """;

    private static final String UPDATE_TRANSACTION_STATUS = """
                UPDATE transactions SET status = :status, updated_at = CURRENT_TIMESTAMP
                WHERE token = :token
            """;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public void insert(Transaction transaction) {
        Map<String, Object> params = getInsertParams(transaction);
        try {
            jdbcTemplate.update(INSERT_QUERY, params);
            log.info("Transaction inserted successfully: {}", transaction);
        } catch (Exception e) {
            log.error("Failed to insert transaction: {}", transaction, e);
            throw e;
        }
    }

    public void updateStatus(String token, TransactionStatus status) {
        Map<String, Object> params = new HashMap<>();
        params.put("status", status.name());
        params.put("token", token);
        try {
            jdbcTemplate.update(UPDATE_TRANSACTION_STATUS, params);
            log.info("Transaction status updated successfully - token {} - status {}", token, status.name());
        } catch (Exception ex) {
            log.error("Failed to update transaction status - token {} - status {}", token, status.name(), ex);
        }
    }

    private Map<String, Object> getInsertParams(Transaction transaction) {
        Map<String, Object> params = new HashMap<>();
        params.put("nuTransaction", transaction.getNuTransaction());
        params.put("token", transaction.getToken());
        params.put("from", transaction.getFrom());
        params.put("to", transaction.getTo());
        params.put("amount", transaction.getAmount());
        params.put("rate", transaction.getRate());
        params.put("comments", transaction.getComments());
        params.put("status", transaction.getStatus().name()); // Assumindo que é um enum
        params.put("createdAt", transaction.getCreatedAt());
        params.put("updatedAt", transaction.getUpdatedAt());
        params.put("transaction_type", transaction.getType().name());
        return params;
    }

}
