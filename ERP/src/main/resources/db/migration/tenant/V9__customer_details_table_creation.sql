CREATE SEQUENCE IF NOT EXISTS customer_details_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS customer_details (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(255) NOT NULL,
    company VARCHAR(255),
    engagement VARCHAR(255),
    lead_id BIGINT,
    createdby_id BIGINT,
    createddate TIMESTAMP,
    lastmodifiedby_id BIGINT,
    lastmodifieddate TIMESTAMP,
    FOREIGN KEY (lead_id) REFERENCES lead_details(id)
);