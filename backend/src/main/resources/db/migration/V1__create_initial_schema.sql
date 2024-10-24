create table app_user
(
    user_id    bigserial
        primary key,
    email      varchar(255) not null,
    first_name varchar(255),
    last_name  varchar(255),
    login      varchar(255) not null,
    password   varchar(255) not null,
    authority  varchar(255) not null
);

CREATE TABLE items (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       description VARCHAR(255),
                       base_price INT NOT NULL,                    -- Основная цена
                       quantity INT NOT NULL                        -- Доступное количество
);

-- Создание таблицы для хранения заказов
CREATE TABLE orders (
                        id SERIAL PRIMARY KEY,               -- Уникальный идентификатор заказа (AUTO_INCREMENT)
                        user_id INT NOT NULL,                -- Внешний ключ для пользователя
                        status int NOT NULL,         -- Статус заказа (например, NEW, IN_PROGRESS, COMPLETED)
                        CONSTRAINT fk_user_order FOREIGN KEY (user_id) REFERENCES app_user(user_id) ON DELETE CASCADE
);

-- Создание таблицы для хранения элементов заказа
CREATE TABLE order_items (
                             id SERIAL PRIMARY KEY,               -- Уникальный идентификатор элемента заказа (AUTO_INCREMENT)
                             order_id INT NOT NULL,               -- Внешний ключ для заказа
                             product_id INT NOT NULL,             -- Внешний ключ для товара (или продукта)
                             quantity INT NOT NULL,               -- Количество товара в заказе
                             price DECIMAL(10, 2) NOT NULL,       -- Цена товара
                             CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);

INSERT INTO app_user(email, first_name, last_name, login, password, authority) VALUES ('client1@example.com','client1','client1','client1','$2a$10$RuZPUZUn7hhJ7RjMkziGX.24LcpAROJTtZ9aIKNQ/cUBxiSLdkG.i','CLIENT');
INSERT INTO app_user(email, first_name, last_name, login, password, authority) VALUES ('client2@example.com','client2','client2','client2','$2a$10$RuZPUZUn7hhJ7RjMkziGX.24LcpAROJTtZ9aIKNQ/cUBxiSLdkG.i','CLIENT');
INSERT INTO app_user(email, first_name, last_name, login, password, authority) VALUES ('client3@example.com','client3','client3','client3','$2a$10$RuZPUZUn7hhJ7RjMkziGX.24LcpAROJTtZ9aIKNQ/cUBxiSLdkG.i','CLIENT');


-- Вставка товаров (айтемов)
INSERT INTO items (name, description, base_price, quantity) VALUES ('Item1', 'Description for Item1', 10, 100);
INSERT INTO items (name, description, base_price, quantity) VALUES ('Item2', 'Description for Item2', 20, 50);

-- Создание заказа для пользователя
-- INSERT INTO orders (user_id, status) VALUES (1, 'NEW');
--
-- -- Добавление айтемов в заказ
-- INSERT INTO order_items (user_order_id, item_id, quantity) VALUES (1, 1, 2);  -- 2 шт. Item1 в заказе
-- INSERT INTO order_items (user_order_id, item_id, quantity) VALUES (1, 2, 1);  -- 1 шт. Item2 в заказе
