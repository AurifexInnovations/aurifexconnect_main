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


CREATE TABLE IF NOT EXISTS tenant_1_rak_gmail_com.payments (
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
