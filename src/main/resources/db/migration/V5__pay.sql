CREATE TABLE wallets (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         user_id BIGINT NOT NULL UNIQUE,
                         balance DECIMAL(12,2) DEFAULT 0,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         FOREIGN KEY (user_id) REFERENCES users(id)
);
CREATE TABLE transactions (
                              id BIGINT PRIMARY KEY AUTO_INCREMENT,
                              user_id BIGINT NOT NULL,
                              type ENUM('TOP_UP', 'PAYMENT') NOT NULL,
                              amount DECIMAL(12,2) NOT NULL,
                              method VARCHAR(50), -- 'VNPay', 'Wallet'
                              description TEXT,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              FOREIGN KEY (user_id) REFERENCES users(id)
);
CREATE TABLE orders (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        user_id BIGINT NOT NULL,
                        post_id BIGINT NOT NULL,
                        payment_method VARCHAR(50), -- 'WALLET' hoặc 'VNPAY'
                        amount DECIMAL(12,2) NOT NULL,
                        status ENUM('PENDING', 'COMPLETED', 'FAILED') DEFAULT 'PENDING',
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (user_id) REFERENCES users(id),
                        FOREIGN KEY (post_id) REFERENCES posts(id)
);
