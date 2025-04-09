CREATE DATABASE order_management;
USE order_management;

CREATE TABLE users (
  userId INT PRIMARY KEY,
  username VARCHAR(50),
  password VARCHAR(50),
  role VARCHAR(10)
);

CREATE TABLE products (
  productId INT PRIMARY KEY,
  productName VARCHAR(100),
  description TEXT,
  price DOUBLE,
  quantityInStock INT,
  type VARCHAR(20),
  brand VARCHAR(50),        
  warrantyPeriod INT,       
  size VARCHAR(10),         
  color VARCHAR(20)         
);

CREATE TABLE orders (
  orderId INT AUTO_INCREMENT PRIMARY KEY,
  userId INT,
  orderDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (userId) REFERENCES users(userId)
);

CREATE TABLE order_items (
  itemId INT AUTO_INCREMENT PRIMARY KEY,
  orderId INT,
  productId INT,
  quantity INT,
  FOREIGN KEY (orderId) REFERENCES orders(orderId),
  FOREIGN KEY (productId) REFERENCES products(productId)
);


-- Test Data
INSERT INTO users (userId, username, password, role) VALUES 
(1, 'admin1', 'admin123', 'Admin'),
(2, 'siva', 'password123', 'User'),
(3, 'michelle', 'mic123', 'User');


-- Electronics
INSERT INTO products (productId, productName, description, price, quantityInStock, type, brand, warrantyPeriod, size, color) VALUES 
(101, 'Samsung TV', 'Smart LED TV', 45999.00, 10, 'Electronics', 'Samsung', 24, NULL, NULL),
(102, 'iPhone 14', 'Apple smartphone', 78999.00, 5, 'Electronics', 'Apple', 12, NULL, NULL);

-- Clothing
INSERT INTO products (productId, productName, description, price, quantityInStock, type, brand, warrantyPeriod, size, color) VALUES 
(201, 'Men T-Shirt', 'Cotton T-Shirt', 499.00, 50, 'Clothing', NULL, NULL, 'L', 'Black'),
(202, 'Women Kurti', 'Rayon Kurti', 999.00, 30, 'Clothing', NULL, NULL, 'M', 'Blue');


-- Orders placed by siva and michelle
INSERT INTO orders (orderId, userId, orderDate) VALUES 
(1001, 2, NOW()),
(1002, 3, NOW());

-- Order 1001 by siva includes Samsung TV and T-Shirt
INSERT INTO order_items (itemId, orderId, productId, quantity) VALUES 
(1, 1001, 101, 1),
(2, 1001, 201, 2);

-- Order 1002 by michelle includes iPhone and Kurti
INSERT INTO order_items (itemId, orderId, productId, quantity) VALUES 
(3, 1002, 102, 1),
(4, 1002, 202, 1);

-- testing Crud
select * from users;

select * from products;

select * from orders;

select * from order_items;