-- Tạo database
CREATE DATABASE IF NOT EXISTS coffee_management1 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE coffee_management1;

-- Bảng Role
CREATE TABLE `Role` (
    role_id INT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL UNIQUE,
    `description` TEXT,
    deleted BOOLEAN DEFAULT FALSE
);

-- Bảng Permission
CREATE TABLE Permission (
    permission_id INT AUTO_INCREMENT PRIMARY KEY,
    permission_name VARCHAR(50) NOT NULL UNIQUE,
    deleted BOOLEAN DEFAULT FALSE
);

-- Bảng Role_Permission
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
    account_name VARCHAR(50) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    role_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (role_id) REFERENCES Role(role_id) ON DELETE CASCADE
);

-- Bảng Category (dùng VARCHAR thay cho NVARCHAR vì MySQL không có NVARCHAR)
CREATE TABLE Category (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(100) CHARACTER SET utf8mb4 NOT NULL,
    deleted BOOLEAN DEFAULT FALSE
);

-- Bảng Product
CREATE TABLE Product (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(255) CHARACTER SET utf8mb4 NOT NULL,
    `description` TEXT CHARACTER SET utf8mb4,
    price DECIMAL(10,2) NOT NULL,
    category_id INT,
    imgSrc VARCHAR(255),
    deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (category_id) REFERENCES Category(category_id) ON DELETE SET NULL
);

CREATE TABLE Orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    total_amount DECIMAL(10, 2),
    status ENUM('Pending', 'Processing', 'Completed', 'Cancelled'),
    order_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Account(id)
);

create table OrderDetail(
	order_detail_id int auto_increment primary key,
    order_id int not null,
    product_id int not null,
    quantity int not null,
    unit_price decimal(10, 2) not null,
    foreign key (order_id) references Orders(id) on delete cascade,
    foreign key (product_id) references Product(product_id) on delete cascade
);

-- Thêm dữ liệu Category trước (để tránh lỗi khóa ngoại khi chèn Product)
INSERT INTO Category(category_name)
VALUES 
    ('Đồ uống'),
    ('Đồ ăn vặt');

-- Thêm dữ liệu vào bảng Role (chỉ một lần)
INSERT INTO Role (role_name, `description`, deleted) 
VALUES 
    ('Admin', 'Quản trị viên hệ thống', FALSE),
    ('User', 'Người dùng thông thường', FALSE);

-- Thêm dữ liệu vào bảng Product
INSERT INTO Product (product_name, `description`, price, category_id, imgSrc, deleted) 
VALUES
    ('Cà phê sữa', 'Cà phê sữa đá đậm đà', 35000.00, 1, 'https://file.hstatic.net/200000662087/file/cach-pha-ca-phe-muoi-thom-ngon-dung-chuan_64af07ec05bc42828004580f8bddb092.jpg', FALSE),
    ('Cà phê đen', 'Cà phê đen nguyên chất', 30000.00, 1, 'https://shopbanghe.vn/wp-content/uploads/2022/07/ca-phe-den-5.jpg', FALSE),
    ('Cà phê đen', 'Cà phê đen nguyên chất', 30000.00, 1, 'https://tse3.mm.bing.net/th?id=OIP.-kwobbU7-_CkAir5ejO4vgHaFy&pid=Api&P=0&h=180', FALSE);

    
INSERT INTO Account (account_name, `password`, role_id, deleted)
VALUES 
    ('admin123', 'adminpass', 1, FALSE),
    ('user123', 'userpass', 2, FALSE);
    
INSERT INTO Orders (user_id, total_amount, status) VALUES
    (1, 100000.00, 'Pending'),
    (1, 50000.00, 'Processing'),
    (1, 75000.00, 'Completed');
    
INSERT INTO OrderDetail (order_id, product_id, quantity, unit_price) VALUES 
	(1, 4, 1, 30000),  
	(1, 5, 1, 35000),  
	(1, 6, 1, 25000);


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
