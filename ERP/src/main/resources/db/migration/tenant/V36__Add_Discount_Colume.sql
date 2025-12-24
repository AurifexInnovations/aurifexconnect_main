

-- Sales Table Alter Queries
ALTER TABLE IF EXISTS sales_orders
    ADD COLUMN IF NOT EXISTS discount_price NUMERIC(15, 2),
    ADD COLUMN IF NOT EXISTS subtotal NUMERIC(15, 2),
    ADD COLUMN IF NOT EXISTS tax_amount NUMERIC(15, 2),
    ADD COLUMN IF NOT EXISTS total_amount NUMERIC(15, 2),
    ADD COLUMN IF NOT EXISTS grand_total NUMERIC(15, 2);

ALTER TABLE IF EXISTS saled_order_product_mapper
    DROP COLUMN IF EXISTS subtotal,
    DROP COLUMN IF EXISTS tax_amount,
    DROP COLUMN IF EXISTS total_amount;

ALTER TABLE IF EXISTS sales_order_service_mapper
    DROP COLUMN IF EXISTS subtotal,
    DROP COLUMN IF EXISTS tax_amount,
    DROP COLUMN IF EXISTS total_amount,
    DROP COLUMN IF EXISTS quantity;

ALTER TABLE IF EXISTS task
    ADD COLUMN IF NOT EXISTS invoice_id BIGINT;