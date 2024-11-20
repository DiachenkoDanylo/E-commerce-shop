-- insert data into categories table
INSERT INTO categories (name, description)
VALUES
    ('PC', 'Personal computers and related hardware'),
    ('Smartphone', 'Mobile phones with advanced features'),
    ('Light', 'Lighting fixtures and accessories'),
    ('TV', 'Television sets and accessories'),
    ('Table', 'Furniture for home and office use'),
    ('Chair', 'Seating furniture for various purposes');

-- Insert data into items table
INSERT INTO items (name, description, base_price, quantity, category_id)
VALUES ('Laptop 2024"', 'High-performance laptop with huge amount of hdd and GPU. Based on 3nm processor', 2400, 4, 3),
       ('PC headphones Beast By Bra', 'Best in world noise-cancelling headphones', 199, 11, 3),
       ('OKphone 2', 'Luxury model of OKphone line smartphone', 999, 21, 4),
       ('OKtablet 16gb 11inch', 'Huge tablet with 10 tap-in-one support', 750, 20,3),
       ('OKwatch', 'Feature-packed this year released smartwatch', 340, 10, 4);
