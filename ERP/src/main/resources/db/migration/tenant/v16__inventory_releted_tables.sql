CREATE TABLE varients (
    id SERIAL PRIMARY KEY,
    item_id BIGINT ,
    stock_quantity NUMERIC(10,2),
    selling_price_type VARCHAR(100),
    selling_price NUMERIC(10,2),
    purchase_price_type VARCHAR(100),
    purchase_price NUMERIC(10,2),
    unit_type VARCHAR(50),
    unit_type_value NUMERIC(10,2),
    measurement_type VARCHAR(50),
    measurement NUMERIC(10,2),
    expiry_date TIMESTAMP
);


CREATE TABLE activity_logs (
    id SERIAL PRIMARY KEY,
    inventory_id BIGINT NOT NULL,
    action TEXT,
    quantity NUMERIC(10,2),
    performed_by VARCHAR(255),
    timestamp TIMESTAMP NOT NULL
);

