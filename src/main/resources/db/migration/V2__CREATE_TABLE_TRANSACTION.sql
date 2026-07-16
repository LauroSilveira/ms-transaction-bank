CREATE TABLE IF NOT EXISTS transactions.bank_transaction (
                             id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                             transaction_id UUID NOT NULL UNIQUE,
                             description VARCHAR(255),
                             amount NUMERIC(19, 2) NOT NULL,
                             transfer_at TIMESTAMP NOT NULL,
                             status VARCHAR(20) NOT NULL,
                             transaction_type VARCHAR(255) NOT NULL

);