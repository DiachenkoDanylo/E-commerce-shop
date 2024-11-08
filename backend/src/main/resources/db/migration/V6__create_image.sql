CREATE TABLE images (
    id SERIAL PRIMARY KEY,
    image_url VARCHAR(255) NOT NULL,
    item_id BIGINT NOT NULL,
    FOREIGN KEY (item_id) REFERENCES items (id) ON DELETE CASCADE
);
--
-- ALTER TABLE items
--     ADD COLUMN category_id INT NOT NULL DEFAULT 1,  -- Allow NULL for now
--     ADD CONSTRAINT fk_category
--         FOREIGN KEY (category_id) REFERENCES categories (id);