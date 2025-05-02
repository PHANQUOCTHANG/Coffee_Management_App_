-- Tạo database
CREATE DATABASE IF NOT EXISTS coffee_management
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
USE coffee_management;

-- admin

-- Bảng Role
CREATE TABLE Role (
    role_id INT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(50) CHARACTER SET utf8mb4 NOT NULL UNIQUE,
    description TEXT CHARACTER SET utf8mb4,
    deleted BOOLEAN DEFAULT FALSE
);

-- Bảng Permission
CREATE TABLE Permission (
    permission_id INT AUTO_INCREMENT PRIMARY KEY,
    permission_name VARCHAR(50) CHARACTER SET utf8mb4 NOT NULL UNIQUE,
    deleted BOOLEAN DEFAULT FALSE
);

-- Bảng Role_Permission (liên kết nhiều-nhiều)
CREATE TABLE Role_Permission (
    role_id INT,
    permission_id INT,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES Role(role_id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES Permission(permission_id) ON DELETE CASCADE
);

-- Bảng Account
CREATE TABLE Account (
    id INT AUTO_INCREMENT PRIMARY KEY,
    account_name VARCHAR(50) CHARACTER SET utf8mb4 NOT NULL UNIQUE,
    password VARCHAR(255) CHARACTER SET utf8mb4 NOT NULL,
    role_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (role_id) REFERENCES Role(role_id) ON DELETE CASCADE
);

-- Bảng Category
CREATE TABLE Category (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(100) CHARACTER SET utf8mb4 NOT NULL,
    deleted BOOLEAN DEFAULT FALSE
);

-- Bảng Product
CREATE TABLE Product (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(255) CHARACTER SET utf8mb4 NOT NULL,
    description TEXT CHARACTER SET utf8mb4,
    price DECIMAL(10, 2) NOT NULL,
    category_id INT,
    imgSrc VARCHAR(255) CHARACTER SET utf8mb4,
    status Boolean DEFAULT FALSE ,
    outstanding Boolean DEFAULT FALSE ,
    deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (category_id) REFERENCES Category(category_id) ON DELETE SET NULL
);

-- Bảng Employee
CREATE TABLE Employee (
    employee_id INT AUTO_INCREMENT PRIMARY KEY,
    fullName VARCHAR(200) CHARACTER SET utf8mb4 NOT NULL,
    phone VARCHAR(20) CHARACTER SET utf8mb4 NOT NULL,
    deleted BOOLEAN DEFAULT FALSE
);

-- Bảng Orders
CREATE TABLE Orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    total_amount DECIMAL(10, 2),
    status ENUM('Pending', 'Processing', 'Completed', 'Cancelled') DEFAULT 'Pending',
    order_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Account(id)
);

-- Bảng OrderDetail
CREATE TABLE OrderDetail (
    order_detail_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES Orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES Product(product_id) ON DELETE CASCADE
);

-- client

-- Bảng Cart
CREATE TABLE Cart (
    cart_id INT AUTO_INCREMENT PRIMARY KEY,
    account_id INT NOT NULL,
    deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (account_id) REFERENCES Account(id) ON DELETE CASCADE
);

-- Bảng Cart_Product (nhiều-nhiều giữa Cart và Product)
CREATE TABLE Cart_Product (
    cart_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    deleted BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (cart_id, product_id),
    FOREIGN KEY (cart_id) REFERENCES Cart(cart_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES Product(product_id) ON DELETE CASCADE
);

-- Bảng InformationUser
CREATE TABLE InformationUser (
    informationUser_id INT AUTO_INCREMENT PRIMARY KEY,
    fullName VARCHAR(200) CHARACTER SET utf8mb4 NOT NULL,
    email VARCHAR(100) CHARACTER SET utf8mb4 NOT NULL,
    phone VARCHAR(200) CHARACTER SET utf8mb4 NOT NULL,
    address VARCHAR(500) CHARACTER SET utf8mb4 NOT NULL,
    account_id INT NOT NULL,
    deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (account_id) REFERENCES Account(id) ON DELETE CASCADE
);

CREATE TABLE OrderUser (
	orderUser_id INT AUTO_INCREMENT PRIMARY KEY,
    account_id INT NOT NULL,
    fullName VARCHAR(200) CHARACTER SET utf8mb4 NOT NULL,
    phone VARCHAR(200) ,
    address VARCHAR(500) CHARACTER SET utf8mb4 NOT NULL,
    note TEXT ,
    shippingFee DECIMAL(10,2) NOT NULL ,
    methodPayment VARCHAR(100) ,
	subPrice DECIMAL(10, 2) NOT NULL ,
    discount DECIMAL(10,2) NOT NULL ,
    order_time DATETIME DEFAULT CURRENT_TIMESTAMP ,
    status ENUM('Pending', 'Processing', 'Completed', 'Cancelled') DEFAULT 'Pending',
    FOREIGN KEY (account_id) REFERENCES Account(id) ON DELETE CASCADE
) ;

CREATE TABLE OrderUser_Product(
	orderUser_id INT NOT NULL ,
    product_id INT NOT NULL ,
    quantity INT NOT NULL ,
    FOREIGN KEY (orderUser_id) REFERENCES OrderUser(orderUser_id) ON DELETE CASCADE ,
    FOREIGN KEY (product_id) REFERENCES Product(product_id) ON DELETE CASCADE
) ;

SET SQL_SAFE_UPDATES = 0 ;
delete from product ;

-- Truy vấn kiểm tra
SELECT * FROM Product;
SELECT * FROM `Role`;
SELECT * FROM Account;
SELECT * FROM Category;
SELECT * FROM Role_Permission;
SELECT * FROM Permission;
select * from orders;
select * from product;
select * from OrderDetail;
select * from cart_product ;
select * from OrderUser ;
select * from OrderUser_Product ;
