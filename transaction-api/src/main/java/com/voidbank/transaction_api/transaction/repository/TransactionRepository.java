package com.voidbank.transaction_api.transaction.repository;

import com.voidbank.transaction_api.transaction.model.Transaction;
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
                    updated_at
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
                    :updatedAt
                )
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
        return params;
    }

}
