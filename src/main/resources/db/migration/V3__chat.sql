CREATE TABLE messages (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          conversation_id BIGINT, -- thêm để gom nhóm chat theo 1 đoạn hội thoại
                          sender_id VARCHAR(50) NOT NULL,
                          receiver_id VARCHAR(50) NOT NULL,
                          content TEXT,
                          content_type VARCHAR(50) DEFAULT 'text', -- text, image, file, etc.
                          is_read BOOLEAN DEFAULT FALSE,
                          sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
#                           FOREIGN KEY (sender_id) REFERENCES users(id),
#                           FOREIGN KEY (receiver_id) REFERENCES users(id)
);
CREATE TABLE notifications (
                               id BIGINT PRIMARY KEY AUTO_INCREMENT,
                               user_id BIGINT NOT NULL,
                               type VARCHAR(50),               -- e.g., 'message', 'system', 'post_update'
                               title VARCHAR(255),
                               content TEXT,
#                                target_url VARCHAR(500),       -- để điều hướng khi user click
                               is_read BOOLEAN DEFAULT FALSE,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               FOREIGN KEY (user_id) REFERENCES users(id)
);
