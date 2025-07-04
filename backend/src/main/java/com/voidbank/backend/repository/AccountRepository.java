package com.voidbank.backend.repository;

import com.voidbank.backend.model.Account;
import com.voidbank.backend.model.TransactionEvent;
import com.voidbank.backend.model.enums.DocumentType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
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
@Slf4j
public class AccountRepository {

    // Table name
    private static final String TABLE_ACCOUNTS = "accounts";
    
    // Column names
    private static final String COL_NU_ACCOUNT = "nu_account";
    private static final String COL_DIGIT = "digit";
    private static final String COL_AGENCY = "agency";
    private static final String COL_OWNER_NAME = "owner_name";
    private static final String COL_DOCUMENT = "document";
    private static final String COL_BALANCE = "balance";
    private static final String COL_DOCUMENT_TYPE = "document_type";
    private static final String COL_CREATED_AT = "created_at";
    private static final String COL_UPDATED_AT = "updated_at";
    
    // Parameter names
    private static final String PARAM_FROM = "from";
    private static final String PARAM_TO = "to";
    private static final String PARAM_AMOUNT = "amount";
    
    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static final String QUERY_EXISTS = String.format("""
                SELECT COUNT(*) FROM %s WHERE %s = :%s
            """, TABLE_ACCOUNTS, COL_NU_ACCOUNT, COL_NU_ACCOUNT);

    private static final String QUERY_BALANCE = String.format("""
                SELECT %s FROM %s WHERE %s = :%s
            """, COL_BALANCE, TABLE_ACCOUNTS, COL_NU_ACCOUNT, COL_NU_ACCOUNT);

    private static final String QUERY_ACCOUNT = String.format("""
                SELECT * FROM %s WHERE %s = :%s
            """, TABLE_ACCOUNTS, COL_NU_ACCOUNT, COL_NU_ACCOUNT);

    private static final String UPDATE_DEBIT_BALANCE = String.format("""
                UPDATE %s
                SET %s = %s - :%s,
                    %s = CURRENT_TIMESTAMP
                WHERE %s = :%s
            """, TABLE_ACCOUNTS, COL_BALANCE, COL_BALANCE, PARAM_AMOUNT, COL_UPDATED_AT, COL_NU_ACCOUNT, PARAM_FROM);

    private static final String UPDATE_CREDIT_BALANCE = String.format("""
                UPDATE %s
                SET %s = %s + :%s,
                    %s = CURRENT_TIMESTAMP
                WHERE %s = :%s
            """, TABLE_ACCOUNTS, COL_BALANCE, COL_BALANCE, PARAM_AMOUNT, COL_UPDATED_AT, COL_NU_ACCOUNT, PARAM_TO);

    private static final String EXISTS_BY_DOCUMENT = String.format("""
                SELECT COUNT(*) FROM %s WHERE %s = :%s
            """, TABLE_ACCOUNTS, COL_DOCUMENT, COL_DOCUMENT);

    private static final String SAVE_ACCOUNT = String.format("""
                INSERT INTO %s
                (%s, %s, %s, %s, %s, %s, %s, %s)
                VALUES (:%s, :%s, :%s, :%s, :%s, :%s, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
            """, 
            TABLE_ACCOUNTS,
            COL_DIGIT, COL_AGENCY, COL_OWNER_NAME, COL_DOCUMENT, COL_BALANCE, COL_DOCUMENT_TYPE, COL_CREATED_AT, COL_UPDATED_AT,
            COL_DIGIT, COL_AGENCY, COL_OWNER_NAME, COL_DOCUMENT, COL_BALANCE, COL_DOCUMENT_TYPE);

    public void createAccount(Account account) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(COL_DIGIT, account.getDigit());
        params.addValue(COL_AGENCY, account.getAgency());
        params.addValue(COL_OWNER_NAME, account.getOwnerName());
        params.addValue(COL_DOCUMENT, account.getDocument());
        params.addValue(COL_BALANCE, account.getBalance());
        params.addValue(COL_DOCUMENT_TYPE, account.getDocumentType().name());

        try {
            jdbcTemplate.update(SAVE_ACCOUNT, params);
        } catch (DataAccessException ex) {
            log.error("Error saving account to database - account {}", account, ex);
            throw ex;
        }
    }

    public boolean accountExists(Long nuAccount) {
        MapSqlParameterSource params = new MapSqlParameterSource(COL_NU_ACCOUNT, nuAccount);
        Integer count = jdbcTemplate.queryForObject(QUERY_EXISTS, params, Integer.class);
        return count != null && count > 0;
    }

    public BigDecimal getBalance(Long nuAccount) {
        MapSqlParameterSource params = new MapSqlParameterSource(COL_NU_ACCOUNT, nuAccount);
        return jdbcTemplate.queryForObject(QUERY_BALANCE, params, BigDecimal.class);
    }

    public Optional<Account> findById(Long nuAccount) {
        MapSqlParameterSource params = new MapSqlParameterSource(COL_NU_ACCOUNT, nuAccount);
        return jdbcTemplate.query(QUERY_ACCOUNT, params, accountRowMapper())
                .stream()
                .findFirst();
    }

    public void updateBalances(TransactionEvent event) {
        Map<String, Object> params = new HashMap<>();
        params.put(PARAM_FROM, event.getFrom());
        params.put(PARAM_TO, event.getTo());
        params.put(PARAM_AMOUNT, event.getAmount());

        jdbcTemplate.update(UPDATE_DEBIT_BALANCE, params);
        jdbcTemplate.update(UPDATE_CREDIT_BALANCE, params);
    }

    public boolean existsByDocument(String document) {
        Map<String, Object> params = new HashMap<>();
        params.put(COL_DOCUMENT, document);
        Integer count = jdbcTemplate.queryForObject(EXISTS_BY_DOCUMENT, params, Integer.class);
        return count != null && count > 0;
    }

    private RowMapper<Account> accountRowMapper() {
        return (rs, rowNum) -> {
            Account account = new Account();
            account.setNuAccount(rs.getLong(COL_NU_ACCOUNT));
            account.setDigit(rs.getInt(COL_DIGIT));
            account.setAgency(rs.getInt(COL_AGENCY));
            account.setOwnerName(rs.getString(COL_OWNER_NAME));
            account.setDocument(rs.getString(COL_DOCUMENT));
            account.setBalance(rs.getBigDecimal(COL_BALANCE));
            account.setDocumentType(DocumentType.valueOf(rs.getString(COL_DOCUMENT_TYPE)));
            account.setCreatedAt(rs.getTimestamp(COL_CREATED_AT).toLocalDateTime());
            account.setUpdatedAt(rs.getTimestamp(COL_UPDATED_AT).toLocalDateTime());
            return account;
        };
    }
}