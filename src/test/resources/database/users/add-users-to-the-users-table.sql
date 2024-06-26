INSERT INTO users (id, email, password, first_name, last_name, is_deleted)
VALUES (4, 'customer@gmail.com', '12345678', 'Oleh', 'Popovich', false),
       (5, 'manager@gmail.com', '12345678', 'Oleh', 'Popovich', false),
       (6, 'user2@example.com', 'hashedPassword', 'John2', 'Doe2', false);

INSERT INTO users_roles (user_id, role_id) VALUES (4, 2),(5, 1),(6, 2);