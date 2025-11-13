
CREATE TABLE IF NOT EXISTS  leads (
    id BIGSERIAL PRIMARY KEY,
    lead_name VARCHAR(255) NOT NULL,
    company_name VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(20),
    source VARCHAR(100),
    type_of_lead VARCHAR(100),
    lead_status VARCHAR(50) CHECK (lead_status IN
        ('NEW','CONTACTED','QUALIFIED','CONVERTED','LOST')) DEFAULT 'NEW',
    engagement_score INT DEFAULT 0,
    remarks TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS lead_product_mapper (
    id BIGSERIAL PRIMARY KEY,
    lead_id BIGINT NOT NULL ,
    product_id BIGINT NOT NULL,
    quantity INT DEFAULT 1
);


CREATE TABLE IF NOT EXISTS customer (
    id BIGSERIAL PRIMARY KEY,
    customer_name VARCHAR(255) NOT NULL,
    company_name VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(20),
    address_line_1 TEXT,
    address_line_2 TEXT,
    landmark VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(100),
    country VARCHAR(100),
    pincode VARCHAR(20),
    tags VARCHAR(255),
    customer_status VARCHAR(50) CHECK (customer_status IN ('ACTIVE','INACTIVE')) DEFAULT 'ACTIVE',
    joined_date DATE DEFAULT CURRENT_DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS customer_details_mapper (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL ,
    product_id BIGINT NOT NULL,
    quantity INT DEFAULT 1
);

CREATE TABLE IF NOT EXISTS follow_up_details (
    id BIGSERIAL PRIMARY KEY,
    lead_id BIGINT,
    customer_id BIGINT,
    follow_up_type VARCHAR(50) CHECK (follow_up_type IN ('CALL','EMAIL','VISIT','MEETING','OTHER')),
    notes TEXT,
    next_follow_up_date DATE,
    next_follow_up_time TIME,
    status VARCHAR(50) CHECK (status IN
        ('SCHEDULED','COMPLETED','PENDING','OVERDUE','ESCALATED')) DEFAULT 'SCHEDULED',
    completion_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

