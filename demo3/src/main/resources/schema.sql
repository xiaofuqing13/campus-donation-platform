CREATE TABLE IF NOT EXISTS donation_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    description TEXT,
    quantity INT NOT NULL,
    status VARCHAR(20) NOT NULL,
    create_time DATETIME NOT NULL
);

CREATE TABLE IF NOT EXISTS donation_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    item_id BIGINT NOT NULL,
    donor_name VARCHAR(100) NOT NULL,
    donor_contact VARCHAR(100) NOT NULL,
    quantity INT NOT NULL,
    donation_time DATETIME NOT NULL,
    status VARCHAR(20) NOT NULL,
    FOREIGN KEY (item_id) REFERENCES donation_items(id)
); 