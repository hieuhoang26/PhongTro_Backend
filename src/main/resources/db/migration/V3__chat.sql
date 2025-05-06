-- Bảng conversations:
CREATE TABLE conversations (
                               id BIGINT PRIMARY KEY AUTO_INCREMENT,
                               user1_id BIGINT NOT NULL,
                               user2_id BIGINT NOT NULL,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               UNIQUE KEY unique_conversation (
                                   (CASE WHEN user1_id < user2_id THEN user1_id ELSE user2_id END),
                                   (CASE WHEN user1_id < user2_id THEN user2_id ELSE user1_id END)
                                   ),
                               FOREIGN KEY (user1_id) REFERENCES users(id),
                               FOREIGN KEY (user2_id) REFERENCES users(id)
);

-- Bảng messages:
CREATE TABLE messages (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          conversation_id BIGINT NOT NULL,
                          sender_id BIGINT NOT NULL,
                          content TEXT,
                          content_type VARCHAR(50) DEFAULT 'text', -- text, image, file, etc.
                          is_read BOOLEAN DEFAULT FALSE,
                          sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (conversation_id) REFERENCES conversations(id),
                          FOREIGN KEY (sender_id) REFERENCES users(id)
#                               INDEX idx_conversation (conversation_id),
#                           INDEX idx_sent_at (sent_at),
#                           INDEX idx_conversation_read (conversation_id, is_read)
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
