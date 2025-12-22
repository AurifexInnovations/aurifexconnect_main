
ALTER TABLE IF EXISTS task_documents
    ADD COLUMN IF NOT EXISTS document_type TEXT;