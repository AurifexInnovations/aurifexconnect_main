CREATE TABLE IF NOT EXISTS invoices (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    sales_order_id BIGINT,
    quotation_id BIGINT,
    invoice_number VARCHAR(50) NOT NULL UNIQUE,
    invoice_date DATE,
    due_date DATE,
    service_category VARCHAR(20),
    sqft NUMERIC(10, 2),
    invoice_is_for VARCHAR(50),
    subtotal NUMERIC(12, 2),
    tax_amount NUMERIC(12, 2),
    total_amount NUMERIC(12, 2),
    discount_amount NUMERIC(12, 2) DEFAULT 0.00,
    grand_total NUMERIC(12, 2),
    amount_paid NUMERIC(12, 2) DEFAULT 0.00,
    balance_amount NUMERIC(12, 2) DEFAULT 0.00,
    status VARCHAR(30) NOT NULL,
    notes TEXT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);


CREATE TABLE IF NOT EXISTS payments (
    id BIGSERIAL PRIMARY KEY,

    invoice_id BIGINT,
    customer_id BIGINT,

    invoice_amount NUMERIC(12, 2),
    amount_paid NUMERIC(12, 2),

    total_paid_till_now NUMERIC(12, 2) DEFAULT 0,
    balance_amount NUMERIC(12, 2) DEFAULT 0,

    payment_status VARCHAR(50),
    payment_method VARCHAR(50),

    transaction_reference VARCHAR(255),

    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    notes TEXT,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE IF NOT EXISTS receipts (
    id BIGSERIAL PRIMARY KEY,

    payment_id BIGINT NOT NULL,
    invoice_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,

    receipt_number VARCHAR(50) NOT NULL UNIQUE,
    receipt_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    amount_received NUMERIC(12,2) NOT NULL,
    payment_method VARCHAR(50),

    notes TEXT,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP


);


CREATE TABLE IF NOT EXISTS saled_order_product_mapper (
    id BIGSERIAL PRIMARY KEY,

    product_id BIGINT NOT NULL,

    saled_order_id BIGINT NOT NULL,

    quantity NUMERIC(12,2) NOT NULL,

    subtotal NUMERIC(12,2),
    tax_amount NUMERIC(12,2),
    total_amount NUMERIC(12,2)
);

CREATE TABLE IF NOT EXISTS sales_order_service_mapper (
    id BIGSERIAL PRIMARY KEY,

    service_id BIGINT NOT NULL,

    sales_order_id BIGINT NOT NULL,

    quantity NUMERIC(12,2) NOT NULL,

    subtotal NUMERIC(12,2),
    tax_amount NUMERIC(12,2),
    total_amount NUMERIC(12,2)
);

CREATE TABLE IF NOT EXISTS sales_orders (
    sales_order_number BIGSERIAL PRIMARY KEY,
    quotation_id BIGINT,
    customer_id BIGINT NOT NULL,
    phone_number VARCHAR(20),
    alternate_phone_number VARCHAR(20),
    sales_order_date DATE,
    customer_name VARCHAR(40),
    company_name VARCHAR(150),
    email VARCHAR(150),
    address TEXT,
    landmark TEXT,
    city VARCHAR(100),
    state VARCHAR(100),
    country VARCHAR(100),
    pincode VARCHAR(10),
    location_url TEXT,
    service_category VARCHAR(20),
    sqft NUMERIC(10,2),
    sales_order_type VARCHAR(20) NOT NULL,
    status VARCHAR(30) NOT NULL,
    notes TEXT,
    service_type VARCHAR(30) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

