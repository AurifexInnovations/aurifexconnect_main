CREATE TABLE IF NOT EXISTS contracts (
     id BIGSERIAL PRIMARY KEY,

     customer_id BIGINT,
     quotation_id BIGINT,

     -- Contract Details
     contract_status VARCHAR(50) NOT NULL DEFAULT 'Draft'
         CHECK (contract_status IN ('Draft', 'Active', 'Expired', 'Cancelled')),
     start_date DATE NOT NULL,
     end_date DATE NOT NULL,
     total_value NUMERIC(12,2) NOT NULL CHECK (total_value >= 0),
     service_frequency VARCHAR(50) NOT NULL
         CHECK (service_frequency IN ('One-Time', 'Monthly', 'Quarterly', 'Half-Yearly', 'Yearly')),
     payment_terms VARCHAR(100) NOT NULL,
     is_recurring BOOLEAN DEFAULT TRUE,

     activation_date DATE,
     renewal_date DATE,
     contract_notes TEXT,

     created_by BIGINT,
     created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
     last_modified_by BIGINT,
     last_modified_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
 );
