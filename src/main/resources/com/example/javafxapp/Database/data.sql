
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


INSERT INTO Orders (user_id, total_amount, status) VALUES
    (1, 100000.00, 'Pending'),
    (1, 50000.00, 'Processing'),
    (1, 75000.00, 'Completed');

INSERT INTO OrderDetail (order_id, product_id, quantity, unit_price) VALUES
	(1, 4, 1, 30000),
	(1, 5, 1, 35000),
	(1, 6, 1, 25000);