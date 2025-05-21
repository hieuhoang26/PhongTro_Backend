-- Bảng lưu thông tin xác thực CCCD
CREATE TABLE verify (
                                         id              BIGINT PRIMARY KEY AUTO_INCREMENT,
                                         user_id         BIGINT NOT NULL,
                                         cccd_number VARCHAR(255) NOT NULL,
                                         front_image_url TEXT,
#                                          back_image_url  TEXT,
                                         extracted_name  VARCHAR(255),
                                         extracted_dob   DATE,
                                         extracted_address TEXT,
                                         status          ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
                                         created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                         approved_at     TIMESTAMP NULL,
                                         FOREIGN KEY (user_id) REFERENCES users(id)
);
