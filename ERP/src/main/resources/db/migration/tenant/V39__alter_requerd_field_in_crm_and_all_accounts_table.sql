


ALTER TABLE IF EXISTS customer
ALTER COLUMN customer_status SET DEFAULT 'ACTIVE';


ALTER TABLE IF EXISTS enhance_quotation
RENAME COLUMN lead_type TO quotation_type;

ALTER TABLE IF EXISTS enhance_quotation
RENAME COLUMN service_category TO service_type;

ALTER TABLE IF EXISTS enhance_quotation
ADD COLUMN IF NOT EXISTS branch_id BIGINT;

ALTER TABLE IF EXISTS enhance_quotation
ADD CONSTRAINT fk_enhance_quotation_branch
FOREIGN KEY (branch_id) REFERENCES branch(branch_id);

ALTER TABLE IF EXISTS enhance_quotation
DROP COLUMN IF EXISTS quotation_date;

-- salesOrder
ALTER TABLE IF EXISTS sales_orders
DROP COLUMN IF EXISTS sales_order_date;

ALTER TABLE IF EXISTS sales_orders
RENAME COLUMN address TO address_line_1;

ALTER TABLE IF EXISTS sales_orders
ADD COLUMN IF NOT EXISTS address_line_2 TEXT;

ALTER TABLE IF EXISTS sales_orders
ADD COLUMN IF NOT EXISTS branch_id BIGINT;

ALTER TABLE IF EXISTS sales_orders
ADD CONSTRAINT fk_sales_orders_branch
FOREIGN KEY (branch_id) REFERENCES branch(branch_id);

ALTER TABLE IF EXISTS sales_orders
DROP COLUMN IF EXISTS service_category;

-- Invoice
ALTER TABLE IF EXISTS invoices
DROP COLUMN IF EXISTS quotation_id;

ALTER TABLE IF EXISTS invoices
DROP COLUMN IF EXISTS invoice_date;

ALTER TABLE IF EXISTS invoices
ADD COLUMN IF NOT EXISTS branch_id BIGINT;

ALTER TABLE IF EXISTS invoices
ADD COLUMN IF NOT EXISTS payment_status VARCHAR(20) DEFAULT 'UNPAID';

ALTER TABLE IF EXISTS invoices
ADD CONSTRAINT fk_invoices_branch
FOREIGN KEY (branch_id) REFERENCES branch(branch_id);

-- payment
ALTER TABLE IF EXISTS payments
ADD COLUMN IF NOT EXISTS branch_id BIGINT;

ALTER TABLE IF EXISTS payments
ADD CONSTRAINT fk_payments_branch
FOREIGN KEY (branch_id) REFERENCES branch(branch_id);

-- receipt
ALTER TABLE IF EXISTS receipts
ADD COLUMN IF NOT EXISTS branch_id BIGINT;

ALTER TABLE IF EXISTS receipts
ADD CONSTRAINT fk_receipts_branch
FOREIGN KEY (branch_id) REFERENCES branch(branch_id);



