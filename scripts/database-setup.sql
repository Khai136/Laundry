-- Database setup for Laundry Management System
-- MySQL Database Structure

-- Create database
CREATE DATABASE IF NOT EXISTS laundry_db;
USE laundry_db;

-- Table: users (for login system)
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role VARCHAR(20) DEFAULT 'karyawan',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: customers (data pelanggan)
CREATE TABLE IF NOT EXISTS customers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    address TEXT,
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: service_packages (paket layanan)
CREATE TABLE IF NOT EXISTS service_packages (
    id INT AUTO_INCREMENT PRIMARY KEY,
    package_name VARCHAR(100) NOT NULL,
    description TEXT,
    price_per_kg DECIMAL(10,2) NOT NULL,
    duration_days INT DEFAULT 3,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: laundry_orders (cucian masuk)
CREATE TABLE IF NOT EXISTS laundry_orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    package_id INT NOT NULL,
    weight_kg DECIMAL(5,2) NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) DEFAULT 'pending',
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    pickup_date TIMESTAMP NULL,
    notes TEXT,
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE,
    FOREIGN KEY (package_id) REFERENCES service_packages(id) ON DELETE CASCADE
);

-- Table: payments (transaksi pembayaran)
CREATE TABLE IF NOT EXISTS payments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    payment_method VARCHAR(20) DEFAULT 'cash',
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'paid',
    FOREIGN KEY (order_id) REFERENCES laundry_orders(id) ON DELETE CASCADE
);

-- Insert default users (bos & karyawan)
-- Bos (pemilik) — akses penuh
INSERT IGNORE INTO users (username, password, full_name, role)
VALUES ('bos', 'bos123', 'Pemilik Laundry', 'bos');

-- Karyawan — akses terbatas
INSERT IGNORE INTO users (username, password, full_name, role)
VALUES ('karyawan', 'kar123', 'Pegawai Laundry', 'karyawan');

-- Legacy admin (tetap ada untuk kompatibilitas)
INSERT IGNORE INTO users (username, password, full_name, role)
VALUES ('admin', 'admin123', 'Administrator', 'bos');

-- Insert sample service packages
INSERT IGNORE INTO service_packages (package_name, description, price_per_kg, duration_days) 
VALUES 
('Cuci Kering', 'Cuci dan kering biasa', 5000.00, 2),
('Cuci Setrika', 'Cuci, kering, dan setrika', 7000.00, 3),
('Express', 'Layanan kilat 1 hari', 10000.00, 1),
('Dry Clean', 'Cuci kering khusus', 15000.00, 3);

-- Insert sample customers
INSERT IGNORE INTO customers (name, phone, address, email) 
VALUES 
('John Doe', '081234567890', 'Jl. Merdeka No. 123', 'john@email.com'),
('Jane Smith', '081987654321', 'Jl. Sudirman No. 456', 'jane@email.com'),
('Budi Santoso', '081555666777', 'Jl. Gatot Subroto No. 789', 'budi@email.com');
