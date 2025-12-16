-- =========================================
-- QUOTATION MAIN TABLE
-- =========================================
CREATE TABLE IF NOT EXISTS quotation (
    id BIGSERIAL PRIMARY KEY,

    lead_id BIGINT,
    customer_id BIGINT,

    full_name VARCHAR(150) NOT NULL,
    company_name VARCHAR(150),
    email VARCHAR(150),
    phone VARCHAR(20),
    alternate_phone VARCHAR(20),

    address_line_1 TEXT,
    address_line_2 TEXT,
    landmark TEXT,
    city VARCHAR(100),
    state VARCHAR(100),
    country VARCHAR(100),
    pincode VARCHAR(10),

    location_url TEXT,

    quotation_number VARCHAR(50) UNIQUE NOT NULL,
    quotation_date DATE DEFAULT CURRENT_DATE,

    service_category VARCHAR(30)
        CHECK (service_category IN ('RESIDENTIAL','COMMERCIAL')),

    sqft NUMERIC(10,2),

    subtotal NUMERIC(12,2) DEFAULT 0,
    tax_amount NUMERIC(12,2) DEFAULT 0,
    total_amount NUMERIC(12,2) DEFAULT 0,
    discount_amount NUMERIC(12,2) DEFAULT 0,

    grand_total NUMERIC(12,2)
        GENERATED ALWAYS AS (total_amount - discount_amount) STORED,

    status VARCHAR(30)
        CHECK (
            status IN (
                'DRAFT',
                'SENT',
                'ACCEPTED',
                'REJECTED',
                'EXPIRED',
                'CONVERTED'
            )
        ) DEFAULT 'DRAFT',

    sent_date TIMESTAMP,
    sent_via VARCHAR(30)
        CHECK (sent_via IN ('EMAIL','WHATSAPP','MANUAL')),

    is_recurring BOOLEAN DEFAULT FALSE,
    recurring_type VARCHAR(20)
        CHECK (recurring_type IN ('MONTHLY','YEARLY')),
    recurring_interval INT,
    recurring_cycles INT,
    start_date DATE,
    next_recurring_date DATE,
    end_date DATE,

    notes TEXT,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =========================================
-- QUOTATION PRODUCT TABLE
-- =========================================
CREATE TABLE IF NOT EXISTS quotation_product (
    id BIGSERIAL PRIMARY KEY,
    quotation_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT DEFAULT 1
);

-- =========================================
-- QUOTATION SERVICE TABLE
-- =========================================
CREATE TABLE IF NOT EXISTS quotation_service (
    id BIGSERIAL PRIMARY KEY,
    quotation_id BIGINT NOT NULL,
    service_id BIGINT NOT NULL
);


ALTER TABLE IF EXISTS quotation
RENAME TO enhance_quotation;


ALTER TABLE IF EXISTS enhance_quotation
ADD COLUMN IF NOT EXISTS lead_type VARCHAR(30)
    CHECK (lead_type IN ('SERVICE', 'PRODUCT'))
    DEFAULT 'SERVICE';

ALTER TABLE IF EXISTS enhance_quotation
ALTER COLUMN quotation_number DROP NOT NULL;
