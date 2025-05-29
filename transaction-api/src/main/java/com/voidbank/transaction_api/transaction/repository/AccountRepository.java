package com.voidbank.transaction_api.transaction.repository;

import com.voidbank.transaction_api.transaction.model.Account;
import com.voidbank.transaction_api.transaction.model.DocumentType;
import com.voidbank.transaction_api.transaction.model.TransactionEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AccountRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static final String QUERY_EXISTS = """
                SELECT COUNT(*) FROM accounts WHERE nu_account = :nu_account
            """;

    private static final String QUERY_BALANCE = """
                SELECT balance FROM accounts WHERE nu_account = :nu_account
            """;

    private static final String QUERY_ACCOUNT = """
                SELECT * FROM accounts WHERE nu_account = :nu_account
            """;

    private static final String UPDATE_DEBIT_BALANCE = """
                UPDATE accounts
                SET balance = balance - :totalDebit,
                    updated_at = CURRENT_TIMESTAMP
                WHERE nu_account = :from
            """;

    private static final String UPDATE_CREDIT_BALANCE = """
                UPDATE accounts
                SET balance = balance + :amount,
                    updated_at = CURRENT_TIMESTAMP
                WHERE nu_account = :to
            """;

    public boolean accountExists(String nuAccount) {
        MapSqlParameterSource params = new MapSqlParameterSource("nu_account", nuAccount);
        Integer count = jdbcTemplate.queryForObject(QUERY_EXISTS, params, Integer.class);
        return count != null && count > 0;
    }

    public BigDecimal getBalance(String nuAccount) {
        MapSqlParameterSource params = new MapSqlParameterSource("nu_account", nuAccount);
        return jdbcTemplate.queryForObject(QUERY_BALANCE, params, BigDecimal.class);
    }

    public Optional<Account> findById(Long nuAccount) {
        MapSqlParameterSource params = new MapSqlParameterSource("nu_account", nuAccount);
        return jdbcTemplate.query(QUERY_ACCOUNT, params, accountRowMapper())
                .stream()
                .findFirst();
    }

    public void updateBalances(TransactionEvent event) {
        BigDecimal amount = event.getAmount();
        BigDecimal rate = event.getRate() != null ? event.getRate() : BigDecimal.ZERO;
        BigDecimal totalDebit = amount.add(rate);

        Map<String, Object> params = new HashMap<>();
        params.put("from", Long.valueOf(event.getFrom()));
        params.put("to", Long.valueOf(event.getTo()));
        params.put("amount", amount);
        params.put("totalDebit", totalDebit);

        jdbcTemplate.update(UPDATE_DEBIT_BALANCE, params);
        jdbcTemplate.update(UPDATE_CREDIT_BALANCE, params);
    }

    private RowMapper<Account> accountRowMapper() {
        return (rs, rowNum) -> {
            Account account = new Account();
            account.setAccountId(rs.getLong("account_id"));
            account.setNuAccount(rs.getLong("nu_account"));
            account.setDigit(rs.getLong("digit"));
            account.setAgency(rs.getLong("agency"));
            account.setOwnerName(rs.getString("owner_name"));
            account.setDocument(rs.getString("document"));
            account.setBalance(rs.getBigDecimal("balance"));
            account.setDocumentType(DocumentType.valueOf(rs.getString("document_type")));
            account.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            account.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
            return account;
        };
    }
}