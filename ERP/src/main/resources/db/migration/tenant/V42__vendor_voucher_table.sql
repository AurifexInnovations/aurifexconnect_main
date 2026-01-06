CREATE TABLE IF NOT EXISTS vendors (
    id BIGSERIAL PRIMARY KEY,

    vendor_code VARCHAR(20) UNIQUE NOT NULL,
    vendor_type VARCHAR(10) NOT NULL,

    company_name VARCHAR(200) NOT NULL,
    contact_person_name VARCHAR(150),
    email VARCHAR(150),
    phone VARCHAR(20),
    alternate_phone VARCHAR(20),

    address_line_1 VARCHAR(255),
    address_line_2 VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(100),
    country VARCHAR(100),
    pincode VARCHAR(10),

    gst_number VARCHAR(20),
    pan_number VARCHAR(20),
    msme_number VARCHAR(30),
    gst_registered BOOLEAN DEFAULT false,

    payment_terms_days INT,
    preferred_currency VARCHAR(10) DEFAULT 'INR',

    bank_name VARCHAR(150),
    bank_account_number VARCHAR(50),
    bank_ifsc_code VARCHAR(20),

    status VARCHAR(20),
    rating INT CHECK (rating BETWEEN 1 AND 5),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS vouchersV1 (
    id BIGSERIAL PRIMARY KEY,

    voucher_no VARCHAR(30) UNIQUE NOT NULL,
    voucher_type VARCHAR(20) NOT NULL,

    voucher_date DATE NOT NULL,
    financial_year VARCHAR(10) NOT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
