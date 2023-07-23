CREATE TABLE spring.events (
	id INT NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    file_id INT NOT NULL,
    status VARCHAR(32) NOT NULL,
	FOREIGN KEY(user_id) REFERENCES spring.users(id),
	FOREIGN KEY(file_id) REFERENCES spring.files(id)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_unicode_ci;
