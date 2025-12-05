
ALTER TABLE IF EXISTS service
    ADD COLUMN IF NOT EXISTS branch_id BIGINT;

ALTER TABLE IF EXISTS service
    ADD CONSTRAINT fk_users_branch
        FOREIGN KEY (branch_id) REFERENCES branch(branch_id);


CREATE TABLE IF NOT EXISTS service_inventory (
    service_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,

    PRIMARY KEY (service_id, item_id),

    CONSTRAINT fk_service
        FOREIGN KEY (service_id) REFERENCES service(service_id),

    CONSTRAINT fk_inventory
        FOREIGN KEY (item_id) REFERENCES inventory(item_id)
);