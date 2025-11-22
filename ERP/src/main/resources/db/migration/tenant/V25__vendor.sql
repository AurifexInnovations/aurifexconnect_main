CREATE TABLE IF NOT EXISTS vendors (
    vendor_id BIGSERIAL PRIMARY KEY,
    vendor_name VARCHAR(200),
    contact_person VARCHAR(150),
    phone_number VARCHAR(20),
    email_address VARCHAR(150) UNIQUE,
    billing_address TEXT,
    payment_terms VARCHAR(50),
    credit_limit DECIMAL(15,2),
    is_active BOOLEAN DEFAULT TRUE,
    created_date TIMESTAMP,
    updated_date TIMESTAMP
);
