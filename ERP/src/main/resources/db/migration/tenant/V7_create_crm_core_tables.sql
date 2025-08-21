-- 1️⃣ Create Accounts Table
CREATE TABLE IF NOT EXISTS accounts (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    industry VARCHAR(255),
    website VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2️⃣ Create Contacts Table
CREATE TABLE IF NOT EXISTS contacts (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255),
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(50),
    account_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_contact_account FOREIGN KEY (account_id) REFERENCES accounts (id)
);
CREATE INDEX IF NOT EXISTS idx_contacts_email ON contacts (email);

-- 3️⃣ Create Deals Table
CREATE TABLE IF NOT EXISTS deals (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    amount NUMERIC(19, 2),
    stage VARCHAR(50) NOT NULL,
    close_date DATE,
    contact_id BIGINT,
    account_id BIGINT,
    assigned_to_id BIGINT,
    assigned_to_name VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_deal_contact FOREIGN KEY (contact_id) REFERENCES contacts (id),
    CONSTRAINT fk_deal_account FOREIGN KEY (account_id) REFERENCES accounts (id)
);

-- 4️⃣ Create Leads Table
CREATE TABLE IF NOT EXISTS leads (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255),
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(50),
    source VARCHAR(255),
    status VARCHAR(50) NOT NULL,
    assigned_to_id BIGINT,
    assigned_to_name VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_leads_email ON leads (email);

-- 5️⃣ Create Notification Messages Table
CREATE TABLE IF NOT EXISTS notification_message (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255),
    message TEXT,
    timestamp BIGINT,
    from_user VARCHAR(255),
    to_user VARCHAR(255)
);