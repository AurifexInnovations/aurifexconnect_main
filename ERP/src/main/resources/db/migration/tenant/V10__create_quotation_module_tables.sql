-- V7_create_quotation_module_tables.sql
-- Description: Creates tables for Quotation Module (Quotation, Quotation Items, Technician Assignment, Status Log)

CREATE TABLE IF NOT EXISTS quotations (
    id SERIAL PRIMARY KEY,
    quotation_id VARCHAR(50) UNIQUE,
    type VARCHAR(20),
    customer_id VARCHAR(50),
    address TEXT,
    contact_person VARCHAR(100),
    quotation_date DATE,
    validity_date DATE,
    payment_terms TEXT,
    total_amount NUMERIC(10,2),
    status VARCHAR(20) DEFAULT 'draft',
    notes TEXT,
    language VARCHAR(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS quotation_items (
    id SERIAL PRIMARY KEY,
    quotation_id VARCHAR(50),
    name VARCHAR(100),
    description TEXT,
    quantity INT,
    unit_price NUMERIC(10,2),
    discount NUMERIC(10,2),
    tax NUMERIC(5,2),
    FOREIGN KEY (quotation_id) REFERENCES quotations(quotation_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS technician_assignment (
    id SERIAL PRIMARY KEY,
    quotation_id VARCHAR(50),
    technician_id VARCHAR(50),
    frequency VARCHAR(20),
    duration VARCHAR(50),
    FOREIGN KEY (quotation_id) REFERENCES quotations(quotation_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS status_log (
    id SERIAL PRIMARY KEY,
    quotation_id VARCHAR(50),
    old_status VARCHAR(20),
    new_status VARCHAR(20),
    changed_by VARCHAR(50),
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (quotation_id) REFERENCES quotations(quotation_id) ON DELETE CASCADE
);
