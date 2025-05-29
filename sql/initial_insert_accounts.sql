INSERT INTO accounts (
    nu_account, digit, agency,
    owner_name, document, balance, document_type,
    created_at, updated_at
) VALUES
('000001', 1, 101, 'Bruce Wayne', '12345678900', 10000.00, 'CPF', NOW(), NOW()),
('000002', 1, 101, 'Clark Kent', '98765432100', 8000.00, 'CPF', NOW(), NOW()),
('000003', 1, 101, 'Diana Prince', '11122233344', 12000.00, 'CPF', NOW(), NOW()),
('000004', 1, 101, 'Barry Allen', '22233344455', 5000.00, 'CPF', NOW(), NOW()),
('000005', 1, 101, 'Hal Jordan', '33344455566', 7000.00, 'CPF', NOW(), NOW());