CREATE TABLE report_posts (
                              id         BIGINT PRIMARY KEY AUTO_INCREMENT,
                              post_id    BIGINT NOT NULL,
                              user_id    BIGINT NOT NULL,
                              reason     TEXT,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              UNIQUE (post_id, user_id),
                              FOREIGN KEY (post_id) REFERENCES posts(id),
                              FOREIGN KEY (user_id) REFERENCES users(id)
);
-- INSERT INTO report_posts (post_id, user_id, reason)
-- VALUES
--     (1, 1, 'Bài đăng chứa thông tin sai sự thật'),
--     (3, 1, 'Giá thuê không đúng như mô tả'),
--     (5, 1, 'Thông tin liên hệ không hợp lệ'),
--     (7, 1, 'Hình ảnh không khớp với thực tế'),
--     (9, 1, 'Bài viết có dấu hiệu lừa đảo');
