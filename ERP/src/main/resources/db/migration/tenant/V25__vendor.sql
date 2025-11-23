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


CREATE TABLE IF NOT EXISTS purchase_orders (
    po_id BIGSERIAL PRIMARY KEY,
    vendor_id BIGINT REFERENCES vendors(vendor_id),
    date_issued DATE NOT NULL,
    expected_delivery_date DATE NOT NULL,
    shipping_cost DECIMAL(15,2),
    total_value DECIMAL(15,2),
    status VARCHAR(30) DEFAULT 'Draft',
    po_number VARCHAR(50) UNIQUE,
    is_active BOOLEAN DEFAULT TRUE,
    created_by BIGINT,
    created_date TIMESTAMP,
    updated_date TIMESTAMP
);

CREATE TABLE purchase_order_items (
    item_id BIGSERIAL PRIMARY KEY,
    po_id BIGINT REFERENCES purchase_orders(po_id),
    product_id BIGINT NOT NULL,
    quantity DECIMAL(15,2) NOT NULL,
    unit_price DECIMAL(15,2),
    discount_percent DECIMAL(5,2),
    subtotal DECIMAL(15,2),
    is_active BOOLEAN DEFAULT TRUE
);


    CREATE TABLE IF NOT EXISTS bills (
        bill_id BIGSERIAL PRIMARY KEY,
        po_id BIGINT REFERENCES purchase_orders(po_id),
        vendor_id BIGINT REFERENCES vendors(vendor_id),
        bill_date DATE NOT NULL,
        tax_id BIGINT NOT NULL,
        due_date DATE NOT NULL,
        total_amount DECIMAL(15,2),
        status VARCHAR(30) DEFAULT 'Pending',
        bill_number VARCHAR(50) UNIQUE,
        is_active BOOLEAN DEFAULT TRUE,
        created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

    CREATE TABLE IF NOT EXISTS bill_items (
        item_id BIGSERIAL PRIMARY KEY,
        bill_id BIGINT REFERENCES bills(bill_id),
        product_id BIGINT NOT NULL,
        quantity DECIMAL(15,2) NOT NULL,
        unit_price DECIMAL(15,2),
        discount_percent DECIMAL(5,2),
        subtotal DECIMAL(15,2),
        tax_rate DECIMAL(5,2),
        is_active BOOLEAN DEFAULT TRUE
    );


CREATE TABLE IF NOT EXISTS payments (
    payment_id BIGSERIAL PRIMARY KEY,
    bill_id BIGINT,
    vendor_id BIGINT,
    date_paid DATE,
    amount_paid DECIMAL(15,2),
    voucher_id BIGINT,
    payment_method VARCHAR(100),
    payment_number VARCHAR(100),
    notes TEXT,
    is_active BOOLEAN,
    created_date DATE,
    updated_date DATE
);

CREATE TABLE IF NOT EXISTS debit_notes (
    dn_id BIGSERIAL PRIMARY KEY,
    bill_id BIGINT,
    vendor_id BIGINT,
    date_issued DATE,
    reason TEXT,
    amount_debited DECIMAL(15,2),
    dn_number VARCHAR(50),
    inventory_adjustment BOOLEAN,
    tax_adjustment_amount DECIMAL(15,2),
    status VARCHAR(30),
    is_active BOOLEAN
);
