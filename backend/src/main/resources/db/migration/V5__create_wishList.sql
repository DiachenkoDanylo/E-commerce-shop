CREATE TABLE wishlist
(
    id         SERIAL PRIMARY KEY,
    user_id    BIGINT NOT NULL,
    item_id    BIGINT NOT NULL,
    added_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- To prevent duplicate entries for the same user-item combination, use a unique constraint
ALTER TABLE wishlist
    ADD CONSTRAINT unique_user_item UNIQUE (user_id, item_id);

INSERT INTO wishlist(user_id, item_id)
VALUES (1, 2), (1, 1);