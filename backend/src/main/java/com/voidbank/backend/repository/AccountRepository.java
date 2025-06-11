package com.voidbank.backend.repository;

import com.voidbank.backend.model.Account;
import com.voidbank.backend.model.DocumentType;
import com.voidbank.backend.model.TransactionEvent;
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
                SET balance = balance - :amount,
                    updated_at = CURRENT_TIMESTAMP
                WHERE nu_account = :from
            """;

    private static final String UPDATE_CREDIT_BALANCE = """
                UPDATE accounts
                SET balance = balance + :amount,
                    updated_at = CURRENT_TIMESTAMP
                WHERE nu_account = :to
            """;

    private static final String EXISTS_BY_DOCUMENT = """
                select count(*) from accounts where document = :document
            """;

    public boolean accountExists(Long nuAccount) {
        MapSqlParameterSource params = new MapSqlParameterSource("nu_account", nuAccount);
        Integer count = jdbcTemplate.queryForObject(QUERY_EXISTS, params, Integer.class);
        return count != null && count > 0;
    }

    public BigDecimal getBalance(Long nuAccount) {
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
        Map<String, Object> params = new HashMap<>();
        params.put("from", event.getFrom());
        params.put("to", event.getTo());
        params.put("amount", event.getAmount());

        jdbcTemplate.update(UPDATE_DEBIT_BALANCE, params);
        jdbcTemplate.update(UPDATE_CREDIT_BALANCE, params);
    }

    public boolean existsByDocument(String document) {
        Map<String, Object> params = new HashMap<>();
        params.put("document", document);
        Integer count = jdbcTemplate.queryForObject(EXISTS_BY_DOCUMENT, params, Integer.class);
        return count != null && count > 0;
    }

    private RowMapper<Account> accountRowMapper() {
        return (rs, rowNum) -> {
            Account account = new Account();
            account.setNuAccount(rs.getLong("nu_account"));
            account.setDigit(rs.getInt("digit"));
            account.setAgency(rs.getInt("agency"));
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