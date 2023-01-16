INSERT INTO products (name, price, stock_qty, in_stock) VALUES ('Milk', 3.5, 120, 1);
INSERT INTO products (name, price, stock_qty, in_stock) VALUES ('Water', 1.5, 100, 1);
INSERT INTO products (name, price, stock_qty, in_stock) VALUES ('Tomato', 10.50, 0, 0);

INSERT INTO orders (order_status, total_price) VALUES ('PENDING', 99);

INSERT INTO order_item (order_id, product_id, qty) VALUES (1, 1, 5);