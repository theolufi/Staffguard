INSERT INTO roles (id, name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO users (id, username, password, email) VALUES
    (1, 'admin', '$2a$10$7WiN9lOZxkGNgWd/t6qdeekM05oqaak4IdqeMJoU3iUcGF/MbvgPq', null);
INSERT INTO users_roles (user_id, role_id) VALUES (1,1);
