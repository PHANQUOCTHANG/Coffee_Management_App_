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

-- Insert into Role
INSERT INTO Role (role_name, description) VALUES ('Admin', 'Quyền admin');
INSERT INTO Role (role_name, description) VALUES ('Staff', 'Quyền staff');
INSERT INTO Role (role_name, description) VALUES ('Customer', 'Quyền customer');

-- Insert into Category
INSERT INTO Category (category_name) VALUES
('Cà phê'),
('Trà sữa'),
('Nước ép'),
('Đá xay'),
('Soda');


-- Insert into Product (20 sản phẩm)
INSERT INTO Product (product_name, description, price, category_id, imgSrc, status, outstanding) VALUES
('Cà phê sữa đá', 'Cà phê sữa đậm đà truyền thống', 25000, 1, 'img/cf_suada.jpg', TRUE, TRUE),
('Cà phê đen', 'Cà phê đen nguyên chất', 20000, 1, 'img/cf_den.jpg', TRUE, FALSE),
('Bạc xỉu', 'Sữa nhiều, cà phê ít', 30000, 1, 'img/bacxiu.jpg', TRUE, FALSE),
('Trà sữa trân châu', 'Trà sữa với trân châu đen', 35000, 2, 'img/trasua_tc.jpg', TRUE, TRUE),
('Trà sữa matcha', 'Matcha thơm ngon kết hợp sữa tươi', 40000, 2, 'img/trasua_matcha.jpg', TRUE, FALSE),
('Trà đào cam sả', 'Hương vị đặc biệt với cam sả', 38000, 2, 'img/tradao.jpg', TRUE, FALSE),
('Nước ép cam', 'Cam tươi nguyên chất 100%', 30000, 3, 'img/epcam.jpg', TRUE, TRUE),
('Nước ép dưa hấu', 'Giải khát mùa hè với dưa hấu', 28000, 3, 'img/epduahau.jpg', TRUE, FALSE),
('Nước ép cà rốt', 'Tốt cho sức khỏe', 30000, 3, 'img/epcarot.jpg', TRUE, FALSE),
('Sinh tố bơ', 'Sinh tố bơ béo ngậy', 35000, 4, 'img/sinhtobo.jpg', TRUE, TRUE),
('Sinh tố xoài', 'Xoài chín xay mịn', 34000, 4, 'img/sinhtoxoai.jpg', TRUE, FALSE),
('Sinh tố dâu', 'Dâu tây tươi mát', 36000, 4, 'img/sinhtodau.jpg', TRUE, FALSE),
('Soda việt quất', 'Soda mix việt quất tươi', 32000, 5, 'img/sodavietquat.jpg', TRUE, TRUE),
('Soda chanh dây', 'Chanh dây mát lạnh', 31000, 5, 'img/sodachanhday.jpg', TRUE, FALSE),
('Soda bạc hà', 'Sảng khoái cùng bạc hà', 30000, 5, 'img/sodabacha.jpg', TRUE, FALSE),
('Latte', 'Cà phê với sữa nóng tạo lớp bọt', 45000, 1, 'img/latte.jpg', TRUE, FALSE),
('Cappuccino', 'Cà phê Ý nổi tiếng', 47000, 1, 'img/cappuccino.jpg', TRUE, FALSE),
('Macchiato trà xanh', 'Trà xanh với kem muối', 42000, 2, 'img/matcha_macchiato.jpg', TRUE, FALSE),
('Trà hoa cúc', 'Thư giãn tinh thần', 33000, 2, 'img/trahoacuc.jpg', TRUE, FALSE),
('Trà gừng mật ong', 'Tăng sức đề kháng', 34000, 2, 'img/tragung.jpg', TRUE, FALSE);


-- Insert into Employee
INSERT INTO Employee (fullName, phone) VALUES
('Nguyễn Văn A', '0901234567'),
('Trần Thị B', '0902234567'),
('Lê Văn C', '0903234567'),
('Phạm Thị D', '0904234567'),
('Hoàng Văn E', '0905234567');