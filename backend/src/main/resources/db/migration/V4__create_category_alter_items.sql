CREATE TABLE categories
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(20)  NOT NULL,
    description VARCHAR(100) NOT NULL
);

-- Insert a default category
INSERT INTO categories(name, description)
VALUES ('WITHOUT CATEGORY', 'All items that aren’t added to any category yet');

-- Alter items table
ALTER TABLE items
    ADD COLUMN category_id INT NOT NULL DEFAULT 1,  -- Allow NULL for now
    ADD CONSTRAINT fk_category
        FOREIGN KEY (category_id) REFERENCES categories (id);