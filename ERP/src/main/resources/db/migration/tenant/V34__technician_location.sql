
-- Technician Location Tracking Table

CREATE TABLE IF NOT EXISTS technician_tracker (
    technician_id BIGINT PRIMARY KEY,
    latitude DECIMAL NOT NULL,
    longitude DECIMAL NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);