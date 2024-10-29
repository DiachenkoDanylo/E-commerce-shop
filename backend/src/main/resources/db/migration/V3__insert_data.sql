-- Insert data into app_user
INSERT INTO app_user(email, first_name, last_name, login, password, authority)
VALUES ('client1@example.com', 'client1', 'client1', 'client1',
        '$2a$10$RuZPUZUn7hhJ7RjMkziGX.24LcpAROJTtZ9aIKNQ/cUBxiSLdkG.i', 'CLIENT');
INSERT INTO app_user(email, first_name, last_name, login, password, authority)
VALUES ('client2@example.com', 'client2', 'client2', 'client2',
        '$2a$10$RuZPUZUn7hhJ7RjMkziGX.24LcpAROJTtZ9aIKNQ/cUBxiSLdkG.i', 'CLIENT');
INSERT INTO app_user(email, first_name, last_name, login, password, authority)
VALUES ('client3@example.com', 'client3', 'client3', 'client3',
        '$2a$10$RuZPUZUn7hhJ7RjMkziGX.24LcpAROJTtZ9aIKNQ/cUBxiSLdkG.i', 'ADMIN');
-- Insert data into items table
INSERT INTO items (name, description, base_price, quantity)
VALUES ('Laptop', 'High-performance laptop', 1200, 10),
       ('Headphones', 'Noise-cancelling headphones', 150, 25),
       ('Smartphone', 'Latest model smartphone', 800, 15),
       ('Tablet', 'Compact tablet with stylus support', 500, 20),
       ('Smartwatch', 'Feature-packed smartwatch', 300, 30);

-- Insert data into orders table
-- Assuming user_id 1, 2, and 3 exist in the app_user table
INSERT INTO orders (user_id, status)
VALUES (1, 1), -- NEW status
       (2, 2), -- IN_PROGRESS status
       (3, 3);
-- COMPLETED status

-- Insert data into order_items table
-- Assuming orders and items above have IDs as follows:
-- order_id 1, 2, 3; product_id 1 to 5.
INSERT INTO order_items (order_id, product_id, quantity, price)
VALUES (1, 1, 2, 1200), -- 2 Laptops for order 1
       (1, 2, 1, 150),  -- 1 Headphone for order 1
       (2, 3, 1, 800),  -- 1 Smartphone for order 2
       (2, 4, 2, 500),  -- 2 Tablets for order 2
       (3, 5, 1, 300);
-- 1 Smartwatch for order 3

-- Insert data into item_reviews table for only three items
-- Only items 1, 2, and 4 receive feedback.
INSERT INTO item_reviews (item_id, order_id, rating, comment)
VALUES (1, 1, 5, 'Excellent laptop, highly recommend!'),
       (2, 1, 4, 'Good quality headphones, a bit pricey.'),
       (4, 2, 5, 'Tablet is perfect for drawing and note-taking.');