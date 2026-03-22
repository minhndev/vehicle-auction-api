ALTER TABLE accounts
    ADD COLUMN IF NOT EXISTS reset_password_token VARCHAR(255),
    ADD COLUMN IF NOT EXISTS reset_password_token_expiry TIMESTAMP;

CREATE INDEX IF NOT EXISTS idx_accounts_reset_password_token
    ON accounts(reset_password_token);

