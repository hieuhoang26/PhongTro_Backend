CREATE TABLE favorite_post (
                               user_id BIGINT NOT NULL,
                               post_id BIGINT NOT NULL,
                               created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                               PRIMARY KEY (user_id, post_id),
                               FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                               FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE
);
