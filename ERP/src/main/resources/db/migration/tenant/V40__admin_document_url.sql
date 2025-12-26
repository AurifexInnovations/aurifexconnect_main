
-- Admin Table Alters

ALTER TABLE IF EXISTS user_admin
    ADD COLUMN IF NOT EXISTS document_url TEXT;