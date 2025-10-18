CREATE SEQUENCE IF NOT EXISTS lead_details_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS lead_details (
    id BIGINT PRIMARY KEY DEFAULT nextval('lead_details_seq'),
    name VARCHAR(255),
    contact VARCHAR(255),
    status VARCHAR(255),
    source VARCHAR(255),
    teg VARCHAR(255),
    engagement VARCHAR(255),
    next_follow_up_date TIMESTAMP,
    comments TEXT,
    createdby_id BIGINT,
    createddate TIMESTAMP,
    lastmodifiedby_id BIGINT,
    lastmodifieddate TIMESTAMP
);