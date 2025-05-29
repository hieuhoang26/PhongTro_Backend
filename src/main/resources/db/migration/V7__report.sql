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
