CREATE TABLE IF NOT EXISTS customer_details (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(255) NOT NULL,
    company VARCHAR(255),
    engagement VARCHAR(255),
    lead_id BIGINT,

    -- Optional auditing fields from AbstractAuditable
    created_by BIGSERIAL,
    created_date TIMESTAMP,
    last_modified_by BIGSERIAL,
    last_modified_date TIMESTAMP,

    FOREIGN KEY (lead_id) REFERENCES leads(id)
);