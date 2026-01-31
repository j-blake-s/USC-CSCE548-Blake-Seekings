CREATE DATABASE IF NOT EXISTS online_commerce;
USE online_commerce;

-- Tables are created in order of dependency
CREATE TABLE Customers (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    address TEXT NOT NULL
);

CREATE TABLE Categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description TEXT
);

CREATE TABLE Products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    category_id INT,
    FOREIGN KEY (category_id) REFERENCES Categories(category_id) ON DELETE SET NULL
);

CREATE TABLE Orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10, 2) DEFAULT 0.00,
    status ENUM('Pending', 'Shipped', 'Delivered', 'Cancelled') DEFAULT 'Pending',
    FOREIGN KEY (customer_id) REFERENCES Customers(customer_id) ON DELETE SET NULL
);

CREATE TABLE Order_Items (
    order_item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT,
    product_id INT,
    quantity INT NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES Products(product_id) ON DELETE CASCADE
);

CREATE TABLE Payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT,
    payment_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    amount DECIMAL(10, 2) NOT NULL,
    payment_method ENUM('Credit Card', 'PayPal', 'Bank Transfer') NOT NULL,
    FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE SET NULL
);

CREATE TABLE Shipments (
    shipment_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT,
    address TEXT NOT NULL,
    status ENUM('Processing', 'In Transit', 'Delivered') DEFAULT 'Processing',
    FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE CASCADE
);