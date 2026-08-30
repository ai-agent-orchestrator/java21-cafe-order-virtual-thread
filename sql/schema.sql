DROP DATABASE IF EXISTS cafe_order_db;
CREATE DATABASE cafe_order_db;
USE cafe_order_db;

CREATE TABLE menu (
    menu_id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(30) NOT NULL,
    price INT NOT NULL,
    CONSTRAINT chk_menu_price CHECK (price >= 0),
    CONSTRAINT chk_menu_category CHECK (category IN ('COFFEE', 'TEA', 'ADE', 'DESSERT'))
);

CREATE TABLE cafe_order (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(50) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'WAITING',
    discount_type VARCHAR(30) NOT NULL DEFAULT 'NONE',
    ordered_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_order_status CHECK (status IN ('WAITING', 'MAKING', 'READY', 'COMPLETED', 'CANCELED')),
    CONSTRAINT chk_discount_type CHECK (discount_type IN ('NONE', 'TAKE_OUT'))
);

CREATE TABLE order_item (
    order_item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    menu_id INT NOT NULL,
    quantity INT NOT NULL,
    CONSTRAINT chk_order_item_quantity CHECK (quantity > 0),
    CONSTRAINT fk_order_item_order
        FOREIGN KEY (order_id)
        REFERENCES cafe_order(order_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_order_item_menu
        FOREIGN KEY (menu_id)
        REFERENCES menu(menu_id)
);

CREATE TABLE virtual_thread_log (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NULL,
    experiment_name VARCHAR(100) NOT NULL,
    task_name VARCHAR(100) NOT NULL,
    thread_name VARCHAR(100) NOT NULL,
    thread_type VARCHAR(20) NOT NULL,
    started_at DATETIME NOT NULL,
    waiting_started_at DATETIME NULL,
    waiting_ended_at DATETIME NULL,
    finished_at DATETIME NULL,
    waiting_time_ms INT NULL,
    total_time_ms INT NULL,
    result_status VARCHAR(20) NOT NULL,
    message VARCHAR(500),
    CONSTRAINT chk_virtual_thread_log_thread_type CHECK (thread_type IN ('VIRTUAL', 'PLATFORM')),
    CONSTRAINT chk_virtual_thread_log_result_status CHECK (result_status IN ('SUCCESS', 'FAIL')),
    CONSTRAINT fk_virtual_thread_log_order
        FOREIGN KEY (order_id)
        REFERENCES cafe_order(order_id)
        ON DELETE SET NULL
);

INSERT INTO menu (menu_id, name, category, price) VALUES
(1, '아메리카노', 'COFFEE', 3000),
(2, '카페라떼', 'COFFEE', 4200),
(3, '바닐라라떼', 'COFFEE', 4800),
(4, '캐모마일', 'TEA', 3900),
(5, '레몬에이드', 'ADE', 5200),
(6, '자몽에이드', 'ADE', 5400),
(7, '치즈케이크', 'DESSERT', 6200),
(8, '초코쿠키', 'DESSERT', 2800);

SELECT 'cafe_order_db schema created' AS message;