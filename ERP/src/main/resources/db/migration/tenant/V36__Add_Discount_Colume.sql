
ALTER TABLE sales_orders
ADD COLUMN discount_price NUMERIC(15, 2),
ADD COLUMN subtotal NUMERIC(15, 2),
ADD COLUMN tax_amount NUMERIC(15, 2),
ADD COLUMN total_amount NUMERIC(15, 2),
ADD COLUMN grand_total NUMERIC(15, 2);

ALTER TABLE saled_order_product_mapper
DROP COLUMN subtotal,
DROP COLUMN tax_amount,
DROP COLUMN total_amount;

ALTER TABLE sales_order_service_mapper
DROP COLUMN subtotal,
DROP COLUMN tax_amount,
DROP COLUMN total_amount,
DROP COLUMN quantity;

ALTER TABLE task
ADD COLUMN invoice_id BIGINT NOT NULL;

