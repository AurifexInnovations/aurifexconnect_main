CREATE TABLE modules (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100),
    description VARCHAR(255),
    created_by BIGINT,
    created_at TIMESTAMP ,
    active BOOLEAN
);

CREATE TABLE actions (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100),
    description VARCHAR(255),
    created_by BIGINT,
    created_at TIMESTAMP,
    active BOOLEAN
);

CREATE TABLE roles_action_permissions (
    id BIGSERIAL PRIMARY KEY,
    role_id BIGINT NOT NULL,
    module_id BIGINT NOT NULL,
    action_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    active BOOLEAN,
    CONSTRAINT unique_role_module_action UNIQUE (role_id, module_id, action_id)

);