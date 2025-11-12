CREATE TABLE IF NOT EXISTS  varients (
    id SERIAL PRIMARY KEY,
    item_id BIGINT ,
    stock_quantity DOUBLE PRECISION,
    selling_price_type VARCHAR(100),
    selling_price DOUBLE PRECISION,
    purchase_price_type VARCHAR(100),
    purchase_price DOUBLE PRECISION,
    unit_type VARCHAR(50),
    unit_type_value DOUBLE PRECISION,
    measurement_type VARCHAR(50),
    measurement DOUBLE PRECISION,
    expiry_date TIMESTAMP
);


CREATE TABLE IF NOT EXISTS activity_logs (
    id SERIAL PRIMARY KEY,
    inventory_id BIGINT NOT NULL,
    action TEXT,
    quantity DOUBLE PRECISION,
    performed_by VARCHAR(255),
    timestamp TIMESTAMP NOT NULL
);



ALTER TABLE inventory
ADD COLUMN IF NOT EXISTS brand_name VARCHAR(255),
ADD COLUMN IF NOT EXISTS product_categories VARCHAR(255),
ADD COLUMN IF NOT EXISTS hsn_code VARCHAR(255),
ADD COLUMN IF NOT EXISTS sku_code VARCHAR(255),
ADD COLUMN IF NOT EXISTS ean VARCHAR(255),
ADD COLUMN IF NOT EXISTS is_returnable BOOLEAN,
ADD COLUMN IF NOT EXISTS tax_id BIGINT,
ADD COLUMN IF NOT EXISTS product_status VARCHAR(255),
ADD COLUMN IF NOT EXISTS branch_id BIGINT,
ADD COLUMN IF NOT EXISTS active  BOOLEAN;


ALTER TABLE stocktransfer
ADD COLUMN initiated_by VARCHAR(255),
ADD COLUMN reason VARCHAR(500);



