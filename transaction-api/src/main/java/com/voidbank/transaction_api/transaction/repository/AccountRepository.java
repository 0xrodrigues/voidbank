package com.voidbank.transaction_api.transaction.repository;

import com.voidbank.transaction_api.transaction.model.Account;
import com.voidbank.transaction_api.transaction.model.DocumentType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
@Slf4j
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