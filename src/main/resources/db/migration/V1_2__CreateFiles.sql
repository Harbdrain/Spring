CREATE TABLE spring.files (
	id INT NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY,
	filename VARCHAR(64) NOT NULL,
	location VARCHAR(128) NOT NULL,
    status VARCHAR(32) NOT NULL
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_unicode_ci;
