
-- Subscription Changes
ALTER TABLE public.subscription
DROP COLUMN IF EXISTS subscriptionplan;

ALTER TABLE public.subscription
ADD COLUMN IF NOT EXISTS total_branches VARCHAR(50),
ADD COLUMN IF NOT EXISTS total_technicians VARCHAR(50),
ADD COLUMN IF NOT EXISTS total_amount VARCHAR(50),
ADD COLUMN IF NOT EXISTS created_at TIMESTAMP,
ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;