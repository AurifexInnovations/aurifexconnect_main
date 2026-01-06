CREATE TABLE IF NOT EXISTS purchase_order (
    po_id BIGSERIAL PRIMARY KEY,

    vendor_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    branch_id BIGINT NOT NULL,

    po_date DATE NOT NULL,
    delivery_date DATE NOT NULL,

    status VARCHAR(20) DEFAULT 'DRAFT',
    total_value DECIMAL(12,2),

    inventory_id BIGINT NOT NULL,
    item_name VARCHAR(255),
    qty DECIMAL(10,2),
    measurement_unit VARCHAR(50),
    measurement_value DECIMAL(10,2),
    price DECIMAL(12,2),
    discount DECIMAL(12,2),
    tax_percent DECIMAL(5,2),
    line_total DECIMAL(12,2),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
