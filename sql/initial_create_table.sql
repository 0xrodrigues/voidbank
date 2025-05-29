CREATE TABLE accounts (
    account_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nu_account VARCHAR(20) NOT NULL,
    digit BIGINT NOT NULL,
    agency BIGINT NOT NULL,
    owner_name VARCHAR(255) NOT NULL,
    document VARCHAR(50) NOT NULL,
    balance DECIMAL(19, 4) NOT NULL DEFAULT 0.0000,
    document_type ENUM('CPF', 'CNPJ') NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    UNIQUE KEY uq_nu_account (nu_account)
);

CREATE TABLE transactions (
    nu_transaction BIGINT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(255) NOT NULL,
    from_nu_account VARCHAR(20) NOT NULL,
    to_nu_account VARCHAR(20) NOT NULL,
    amount DECIMAL(19, 4) NOT NULL,
    rate DECIMAL(19, 4),
    comments TEXT,
    status ENUM('PENDING', 'COMPLETED', 'FAILED') NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (from_nu_account) REFERENCES accounts(nu_account),
    FOREIGN KEY (to_nu_account) REFERENCES accounts(nu_account)
);