use railway;

-- Categories (4)
INSERT INTO Categories (name, description) VALUES 
('Electronics', 'Gadgets and tech'), ('Books', 'Educational and fiction'), 
('Home', 'Furniture and decor'), ('Clothing', 'Apparel');

-- Products (8)
INSERT INTO Products (name, description, price, category_id) VALUES 
('Laptop', 'High performance', 1200.00, 1), ('Smartphone', '5G Capable', 800.00, 1),
('Java Book', 'Advanced Guide', 50.00, 2), ('SQL Book', 'DBA Pro', 60.00, 2),
('Sofa', 'Leather 3-seater', 500.00, 3), ('Lamp', 'LED Desk Lamp', 30.00, 3),
('T-Shirt', 'Cotton XL', 20.00, 4), ('Jeans', 'Denim Blue', 45.00, 4);

-- Customers (10)
INSERT INTO Customers (first_name, last_name, email, phone, address) VALUES 
('John', 'Doe', 'john@example.com', '555-0101', '123 Main St'),
('Jane', 'Smith', 'jane@example.com', '555-0102', '456 Oak Ave'),
('Alice', 'Brown', 'alice@example.com', '555-0103', '789 Pine Rd'),
('Bob', 'White', 'bob@example.com', '555-0104', '321 Elm St'),
('Charlie', 'Green', 'charlie@example.com', '555-0105', '654 Maple Dr'),
('David', 'Black', 'david@example.com', '555-0106', '987 Cedar Ln'),
('Eve', 'Grey', 'eve@example.com', '555-0107', '159 Birch Ct'),
('Frank', 'Blue', 'frank@example.com', '555-0108', '753 Walnut St'),
('Grace', 'Rose', 'grace@example.com', '555-0109', '852 Cherry Way'),
('Hank', 'Hill', 'hank@example.com', '555-0110', '951 Willow Blvd');

-- Orders (10)
INSERT INTO Orders (customer_id, total_amount, status) VALUES 
(1, 1250.00, 'Delivered'), (2, 800.00, 'Shipped'), (3, 110.00, 'Pending'),
(4, 500.00, 'Delivered'), (5, 50.00, 'Pending'), (6, 1200.00, 'Shipped'),
(7, 30.00, 'Delivered'), (8, 65.00, 'Pending'), (9, 20.00, 'Delivered'),
(10, 800.00, 'Pending');

-- Order_Items (10)
INSERT INTO Order_Items (order_id, product_id, quantity, unit_price) VALUES 
(1, 1, 1, 1200.00), (1, 3, 1, 50.00), (2, 2, 1, 800.00), (3, 3, 1, 50.00), (3, 4, 1, 60.00),
(4, 5, 1, 500.00), (5, 3, 1, 50.00), (6, 1, 1, 1200.00), (7, 6, 1, 30.00), (8, 8, 1, 45.00);

-- Payments (10)
INSERT INTO Payments (order_id, amount, payment_method) VALUES 
(1, 1250.00, 'Credit Card'), (2, 800.00, 'PayPal'), (3, 110.00, 'Bank Transfer'),
(4, 500.00, 'Credit Card'), (5, 50.00, 'PayPal'), (6, 1200.00, 'Credit Card'),
(7, 30.00, 'Bank Transfer'), (8, 65.00, 'Credit Card'), (9, 20.00, 'PayPal'), (10, 800.00, 'Credit Card');



-- Payments (10)
INSERT INTO Shipments (shipment_id, order_id, address, status) VALUES 
(1, 1, '123 Main St', 'In Transit')
