CREATE TABLE categories (
    id UUID PRIMARY KEY,
    name VARCHAR(150) UNIQUE NOT NULL,
    slug VARCHAR(150) UNIQUE NOT NULL,
    description VARCHAR(250),
    is_active BOOLEAN NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    created_by VARCHAR(150) NOT NULL,
    updated_by VARCHAR(150)
);

CREATE TABLE products (
    id UUID PRIMARY KEY,
    category_id UUID NOT NULL REFERENCES categories(id),
    name VARCHAR(255) NOT NULL,
    brand VARCHAR(100),
    model VARCHAR(100),
    color VARCHAR(20),
    vin_number VARCHAR(50) UNIQUE,
    engine_number VARCHAR(50),
    license_plate VARCHAR(20),
    manufacture_year INTEGER,
    mileage INTEGER,
    transmission VARCHAR(50),
    fuel_type VARCHAR(50),
    description TEXT,
    start_price NUMERIC(19,2),
    status VARCHAR(20),
    is_active BOOLEAN NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    created_by VARCHAR(150) NOT NULL,
    updated_by VARCHAR(150)
);

CREATE TABLE product_images (
    id UUID PRIMARY KEY,
    product_id UUID NOT NULL REFERENCES products(id),
    url TEXT NOT NULL,
    is_main BOOLEAN NOT NULL DEFAULT FALSE,
    sort_order INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP
);

CREATE TABLE auctions (
    id UUID PRIMARY KEY,
    product_id UUID NOT NULL REFERENCES products(id),
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    actual_end_time TIMESTAMP,
    start_price NUMERIC(19,2) NOT NULL,
    current_price NUMERIC(19,2) NOT NULL,
    bid_increment NUMERIC(19,2) NOT NULL,
    deposit_amount NUMERIC(19,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    winner_id UUID,
    version INTEGER,
    is_active BOOLEAN NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    created_by VARCHAR(150) NOT NULL,
    updated_by VARCHAR(150)
);