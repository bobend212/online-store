INSERT INTO products (name, price, stock_qty, in_stock) VALUES ('Milk', 3.5, 10, 1);
INSERT INTO products (name, price, stock_qty, in_stock) VALUES ('Water', 1.5, 10, 1);
INSERT INTO products (name, price, stock_qty, in_stock) VALUES ('Tomato', 10.50, 0, 0);

INSERT INTO orders (order_status) VALUES ('PROCESSING');

INSERT INTO order_item (order_id, product_id, qty) VALUES (1, 1, 5);
-- INSERT INTO order_item (order_id, product_id, qty) VALUES (1, 2, 3);