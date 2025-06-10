-- 1. Roles (must be created first due to foreign key constraints)
CREATE TABLE roles
(
    id   BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) UNIQUE
);

-- 2. Permissions
CREATE TABLE permissions
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(100),
    description TEXT
);

-- 3. Role Permissions (junction table)
CREATE TABLE role_permissions
(
    role_id       BIGINT,
    permission_id BIGINT,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES roles (id),
    FOREIGN KEY (permission_id) REFERENCES permissions (id)
);

-- 4. Users
CREATE TABLE users
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    name       VARCHAR(50) UNIQUE  NOT NULL,
    email      VARCHAR(100) UNIQUE NOT NULL,
    password   VARCHAR(255),
    full_name  VARCHAR(100),
    phone      VARCHAR(20) UNIQUE  NOT NULL,
    avatar_url TEXT,
    role_id    BIGINT,
    is_active  BOOLEAN   DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES roles (id)
);

CREATE TABLE token
(
    id            INT          NOT NULL AUTO_INCREMENT,
    username      VARCHAR(255) NULL,
    refresh_token VARCHAR(255) NULL,
    created_at    TIMESTAMP    NULL,
    updated_at    TIMESTAMP    NULL,
    PRIMARY KEY (id)
);

-- ======== INSERT SAMPLE DATA ========

-- Roles
INSERT INTO roles (name)
VALUES ('USER'),
       ('HOST'),
       ('ADMIN');

-- Permissions
INSERT INTO permissions (name, description)
VALUES ('VIEW_LISTING', 'Xem danh sách bài đăng'),
       ('VIEW_LISTING_DETAIL', 'Xem chi tiết bài đăng'),
       ('CREATE_LISTING', 'Tạo bài đăng mới'),
       ('UPDATE_LISTING', 'Cập nhật bài đăng'),
       ('DELETE_LISTING', 'Xóa bài đăng'),

       ('FAVORITE_LISTING', 'Lưu tin yêu thích'),
       ('COMMENT', 'Bình luận, đánh giá'),
       ('CHAT', 'Gửi tin nhắn trò chuyện'),

       ('VIEW_PROFILE', 'Xem hồ sơ cá nhân'),
       ('UPDATE_PROFILE', 'Cập nhật hồ sơ cá nhân'),

       ('MANAGE_USERS', 'Quản lý người dùng'),
       ('MANAGE_LISTINGS', 'Duyệt hoặc xóa bài đăng'),
       ('MANAGE_CATEGORIES', 'Quản lý danh mục loại phòng'),
       ('MANAGE_SERVICES', 'Quản lý gói dịch vụ VIP, quảng cáo'),
       ('VIEW_STATS', 'Xem thống kê, báo cáo');

-- Role-Permissions

-- USER
# INSERT INTO role_permissions (role_id, permission_id) VALUES
#     (1, 1); -- USER → POST_LISTING
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
         JOIN permissions p ON p.name IN (
                                          'VIEW_LISTING',
                                          'VIEW_LISTING_DETAIL',
                                          'FAVORITE_LISTING',
                                          'COMMENT',
                                          'CHAT',
                                          'VIEW_PROFILE',
                                          'UPDATE_PROFILE'
    )
WHERE r.name = 'USER';


-- HOST
# INSERT INTO role_permissions (role_id, permission_id)
# VALUES (2, 1),
#        (2, 3);
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
         JOIN permissions p ON p.name IN (
                                          'VIEW_LISTING',
                                          'VIEW_LISTING_DETAIL',
                                          'CREATE_LISTING',
                                          'UPDATE_LISTING',
                                          'DELETE_LISTING',
                                          'CHAT',
                                          'VIEW_STATS',
                                          'VIEW_PROFILE',
                                          'UPDATE_PROFILE'
    )
WHERE r.name = 'HOST';


-- ADMIN
# INSERT INTO role_permissions (role_id, permission_id)
# VALUES (3, 1),
#        (3, 2),
#        (3, 3);
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
         JOIN permissions p ON p.name IN (
                                          'MANAGE_USERS',
                                          'MANAGE_LISTINGS',
                                          'MANAGE_CATEGORIES',
                                          'MANAGE_SERVICES',
                                          'VIEW_STATS'
    )
WHERE r.name = 'ADMIN';


-- Users
INSERT INTO users (name, email, password, full_name, phone, avatar_url, role_id)
VALUES ('user1', 'user1@gmail.com', '$2a$10$FDuNMw.4KzfQ5QeuPBRpUe3/6rX1Wr./e1C2qok50tbdHHi9pIZ0S', 'User_Search',
        '123456789', 'https://example.com/avatar1.jpg', 1),
       ('user2', 'user2@example.com', '$2a$10$FDuNMw.4KzfQ5QeuPBRpUe3/6rX1Wr./e1C2qok50tbdHHi9pIZ0S', 'User_Host',
        '011111111', 'https://example.com/avatar2.jpg', 2),
       ('admin', 'admin@example.com', '$2a$10$FDuNMw.4KzfQ5QeuPBRpUe3/6rX1Wr./e1C2qok50tbdHHi9pIZ0S', 'Administrator',
        '987654321', 'https://example.com/avatar2.jpg', 3);
