
-- Leads and Customer Branch Wise Connection
ALTER TABLE IF EXISTS leads
    ADD COLUMN branch_id BIGINT,
    ADD CONSTRAINT fk_branch_leads FOREIGN KEY (branch_id) REFERENCES branch(branch_id);

ALTER TABLE IF EXISTS customer
    ADD COLUMN branch_id BIGINT,
    ADD CONSTRAINT fk_branch_customer FOREIGN KEY (branch_id) REFERENCES branch(branch_id);