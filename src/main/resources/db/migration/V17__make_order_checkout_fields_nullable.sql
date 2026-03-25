ALTER TABLE orders
    ALTER COLUMN recipient_name DROP NOT NULL,
    ALTER COLUMN recipient_phone DROP NOT NULL,
    ALTER COLUMN shipping_address DROP NOT NULL;

