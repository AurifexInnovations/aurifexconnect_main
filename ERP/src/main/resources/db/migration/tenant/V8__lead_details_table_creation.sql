CREATE TABLE IF NOT EXISTS leads (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    contact VARCHAR(255),
    status VARCHAR(255),
    source VARCHAR(255),
    teg VARCHAR(255),
    engagement VARCHAR(255),
    next_follow_up_date TIMESTAMP,
    comments TEXT,

    created_by BIGINT,
    created_date TIMESTAMP,
    last_modified_by BIGINT,
    last_modified_date TIMESTAMP
);