CREATE TABLE IF NOT EXISTS deposits (
    id UUID PRIMARY KEY,
    account_id UUID NOT NULL,
    auction_id UUID NOT NULL,
    amount NUMERIC(19,2) NOT NULL,
    status VARCHAR(255) NOT NULL,
    payment_method VARCHAR(50),
    transaction_reference VARCHAR(100),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    created_by VARCHAR(150) NOT NULL,
    updated_by VARCHAR(150)
);

CREATE TABLE IF NOT EXISTS orders (
    id UUID PRIMARY KEY,
    auction_id UUID NOT NULL,
    winner_id UUID NOT NULL,
    total_amount NUMERIC(19,2) NOT NULL,
    remaining_amount NUMERIC(19,2) NOT NULL,
    status VARCHAR(255) NOT NULL,
    payment_deadline TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    created_by VARCHAR(150) NOT NULL,
    updated_by VARCHAR(150)
);

CREATE TABLE IF NOT EXISTS bids (
    id UUID PRIMARY KEY,
    auction_id UUID NOT NULL,
    bidder_id UUID NOT NULL,
    amount NUMERIC(19,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    bid_time TIMESTAMP NOT NULL,
    ip_address VARCHAR(45),
    user_agent TEXT,
    created_at TIMESTAMP NOT NULL
);
