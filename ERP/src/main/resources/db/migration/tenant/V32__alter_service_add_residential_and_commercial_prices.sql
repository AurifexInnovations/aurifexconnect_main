-- 1. Change service_price from DOUBLE to NUMERIC (money safe)
ALTER TABLE service
ALTER COLUMN service_price
TYPE NUMERIC(15, 2)
USING service_price::NUMERIC;

-- 2. Add residential_price column
ALTER TABLE service
ADD COLUMN residential_price NUMERIC(15, 2);

-- 3. Add commercial_price column
ALTER TABLE service
ADD COLUMN commercial_price NUMERIC(15, 2);
