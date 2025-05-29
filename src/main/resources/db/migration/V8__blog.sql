CREATE TABLE blogs (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       slug VARCHAR(255) UNIQUE NOT NULL,
                       thumbnail VARCHAR(500),
                       description TEXT,
                       content LONGTEXT,
                       status ENUM('DRAFT', 'PUBLISHED', 'DELETED') DEFAULT 'DRAFT',
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
