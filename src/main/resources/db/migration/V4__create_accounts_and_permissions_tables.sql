CREATE TABLE IF NOT EXISTS accounts (
    id UUID PRIMARY KEY,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255),
    is_verified BOOLEAN NOT NULL DEFAULT FALSE,
    failed_attempt_count INT NOT NULL DEFAULT 0,
    last_failed_attempt_at TIMESTAMP WITHOUT TIME ZONE,
    is_system BOOLEAN NOT NULL DEFAULT FALSE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(150) NOT NULL DEFAULT 'SYSTEM',
    updated_by VARCHAR(150) DEFAULT 'SYSTEM',
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    account_id UUID NOT NULL UNIQUE,
    first_name VARCHAR(150) NOT NULL,
    last_name VARCHAR(150) NOT NULL,
    identity_number VARCHAR(20) UNIQUE,
    birthdate DATE,
    gender VARCHAR(20),
    phone_number VARCHAR(20) UNIQUE,
    address TEXT,
    avatar_url TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(150) NOT NULL DEFAULT 'SYSTEM',
    updated_by VARCHAR(150) DEFAULT 'SYSTEM',
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE IF NOT EXISTS account_roles (
    account_id UUID NOT NULL,
    role_id UUID NOT NULL,
    PRIMARY KEY(account_id, role_id)
);

CREATE INDEX idx_accounts_email ON accounts(email);
CREATE INDEX idx_account_roles_account_id ON account_roles(account_id);
CREATE INDEX idx_account_roles_role_id ON account_roles(role_id);