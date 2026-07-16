CREATE TABLE IF NOT EXISTS transactions.audit(
    id             BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    transaction_id UUID NOT NULL,
    failure_reason VARCHAR(255),
    topic_origin   VARCHAR(255) NOT NULL,
    status          VARCHAR(20) NOT NULL,
    transfer_at TIMESTAMP NOT NULL
);