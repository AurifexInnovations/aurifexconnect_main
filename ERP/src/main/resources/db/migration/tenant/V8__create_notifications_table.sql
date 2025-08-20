CREATE TABLE IF NOT EXISTS notifications (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255),
    message TEXT NOT NULL,
    timestamp BIGINT NOT NULL,
    from_user VARCHAR(255),
    to_user VARCHAR(255),
    is_read BOOLEAN DEFAULT FALSE,
    read_at BIGINT,
    type VARCHAR(100)
);

CREATE TABLE token_blacklist (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(500) NOT NULL,
    blacklisted_at TIMESTAMP DEFAULT now()
);