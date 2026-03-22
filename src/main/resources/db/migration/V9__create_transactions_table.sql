CREATE TABLE IF NOT EXISTS transactions (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    reference_id UUID,
    target_type VARCHAR(50),
    gateway_reference VARCHAR(255) UNIQUE,
    amount BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE INDEX idx_transactions_gateway_reference ON transactions(gateway_reference);

CREATE INDEX idx_transactions_user_id ON transactions(user_id);