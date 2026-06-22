-- ============================================
-- Car Booking System - Database Setup
-- ============================================

CREATE DATABASE IF NOT EXISTS car_booking_db;
USE car_booking_db;

-- ============================================
-- Table 1: customers
-- ============================================
CREATE TABLE IF NOT EXISTS customers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    cnic VARCHAR(15) UNIQUE NOT NULL,
    email VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- Table 2: admins
-- ============================================
CREATE TABLE IF NOT EXISTS admins (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    cnic VARCHAR(15) UNIQUE NOT NULL,
    email VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- Table 3: cars  (admin_id added for separation)
-- ============================================
CREATE TABLE IF NOT EXISTS cars (
    id INT AUTO_INCREMENT PRIMARY KEY,
    admin_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    brand VARCHAR(50) NOT NULL,
    year INT NOT NULL,
    price_per_day DOUBLE NOT NULL,
    status VARCHAR(20) DEFAULT 'Available',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (admin_id) REFERENCES admins(id)
);

-- ============================================
-- Table 4: bookings  (admin_id added for separation)
-- ============================================
CREATE TABLE IF NOT EXISTS bookings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    admin_id INT NOT NULL,
    customer_id INT NOT NULL,
    car_id INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status VARCHAR(20) DEFAULT 'Pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (admin_id) REFERENCES admins(id),
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (car_id) REFERENCES cars(id)
);

-- ============================================
-- Sample Admin Accounts
-- Admin 1: CNIC 12345-1234567-1, Password: admin123
-- Admin 2: CNIC 98765-7654321-9, Password: admin456
-- ============================================
INSERT INTO admins (username, password, cnic, email) VALUES
('admin1', 'admin123', '12345-1234567-1', 'admin1@carbooking.com'),
('admin2', 'admin456', '98765-7654321-9', 'admin2@carbooking.com');

-- ============================================
-- Sample Cars for Admin 1 (id=1)
-- ============================================
INSERT INTO cars (admin_id, name, brand, year, price_per_day, status) VALUES
(1, 'Suzuki Alto VXR', 'Suzuki', 2023, 3500, 'Available'),
(1, 'Suzuki Swift', 'Suzuki', 2022, 4500, 'Available'),
(1, 'Honda City Aspire', 'Honda', 2023, 6000, 'Available'),
(1, 'Honda Civic Oriel', 'Honda', 2022, 9000, 'Available'),
(1, 'Toyota Corolla GLI', 'Toyota', 2023, 7000, 'Available');

-- ============================================
-- Sample Cars for Admin 2 (id=2)
-- ============================================
INSERT INTO cars (admin_id, name, brand, year, price_per_day, status) VALUES
(2, 'Toyota Yaris ATIV', 'Toyota', 2022, 5500, 'Available'),
(2, 'Hyundai Tucson', 'Hyundai', 2023, 12000, 'Available'),
(2, 'KIA Sportage', 'KIA', 2023, 11000, 'Available'),
(2, 'MG HS', 'MG', 2023, 13000, 'Available'),
(2, 'Toyota Fortuner', 'Toyota', 2022, 18000, 'Available');
