
CREATE TABLE IF NOT EXISTS contracts (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    quotation_id BIGINT,
    contract_status VARCHAR(50) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    total_value NUMERIC(12,2) NOT NULL CHECK (total_value >= 0),
    service_frequency VARCHAR(50) NOT NULL,
    payment_terms VARCHAR(100) NOT NULL,
    is_recurring BOOLEAN DEFAULT TRUE,
    activation_date DATE,
    renewal_date DATE,
    contract_notes TEXT,
    created_by BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified_by BIGINT,
    last_modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE quotations
ALTER COLUMN customer_id TYPE BIGINT
USING customer_id::BIGINT;
