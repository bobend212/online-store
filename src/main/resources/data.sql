INSERT INTO products (name, price, stock_qty) VALUES ('Milk', 3.5, 10);
INSERT INTO products (name, price, stock_qty) VALUES ('Water', 1.5, 10);
INSERT INTO products (name, price, stock_qty) VALUES ('Tomato', 10.50, 0);

INSERT INTO orders (order_status) VALUES ('PROCESSING');

INSERT INTO order_item (order_id, product_id, qty) VALUES (1, 1, 5);
-- INSERT INTO order_item (order_id, product_id, qty) VALUES (1, 2, 3);