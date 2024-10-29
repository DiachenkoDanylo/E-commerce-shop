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

CREATE TABLE items
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    base_price  INT          NOT NULL, -- Основная цена
    quantity    INT          NOT NULL  -- Доступное количество
);

CREATE TABLE orders
(
    id      SERIAL PRIMARY KEY, -- Уникальный идентификатор заказа (AUTO_INCREMENT)
    user_id INT NOT NULL,       -- Внешний ключ для пользователя
    status  int NOT NULL,       -- Статус заказа (например, NEW, IN_PROGRESS, COMPLETED)
    CONSTRAINT fk_user_order FOREIGN KEY (user_id) REFERENCES app_user (user_id) ON DELETE CASCADE
);

CREATE TABLE order_items
(
    id         SERIAL PRIMARY KEY,      -- Уникальный идентификатор элемента заказа (AUTO_INCREMENT)
    order_id   INT            NOT NULL, -- Внешний ключ для заказа
    product_id INT            NOT NULL, -- Внешний ключ для товара (или продукта)
    quantity   INT            NOT NULL, -- Количество товара в заказе
    price      DECIMAL(10, 2) NOT NULL, -- Цена товара
    CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE
);


