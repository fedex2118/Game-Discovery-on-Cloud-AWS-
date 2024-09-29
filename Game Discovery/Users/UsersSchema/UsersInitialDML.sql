USE users;

-- Generating predefined roles
INSERT INTO users.roles (role_name) VALUES ('ADMIN'), ('USER');

-- Generating admin user -- id: 1
INSERT INTO users.users (username, password, email, role_id, created_at, updated_at) 
VALUES ('admin', '$2a$10$S52DjrttgEi11p4zAq2L7.jailkebcGH3eFW23U8iLRaSt86Ltpt6', 'admin@email.com', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
