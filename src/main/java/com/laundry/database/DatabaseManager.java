package com.laundry.database;

import java.sql.*;

public class DatabaseManager {
    // Konfigurasi database MySQL XAMPP
    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "3306";
    private static final String DB_NAME = "laundry_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = ""; // Default XAMPP password kosong
    
    private static final String DB_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME + 
                                        "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Jakarta";
    
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver tidak ditemukan!", e);
        }
    }
    
    public static void initializeDatabase() {
        try {
            // Test koneksi ke MySQL server
            testConnection();
            
            // Create database if not exists
            createDatabase();
            
            // Execute SQL setup script
            executeSQLScript();
            
            System.out.println("Database berhasil diinisialisasi!");
            System.out.println("Host: " + DB_HOST + ":" + DB_PORT);
            System.out.println("Database: " + DB_NAME);
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Gagal menginisialisasi database: " + e.getMessage());
            System.err.println("\nPastikan XAMPP MySQL sudah berjalan!");
            System.err.println("Buka XAMPP Control Panel dan start Apache & MySQL");
        }
    }
    
    private static void testConnection() throws SQLException {
        String testUrl = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + 
                        "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Jakarta";
        
        try (Connection conn = DriverManager.getConnection(testUrl, DB_USER, DB_PASSWORD)) {
            System.out.println("Koneksi ke MySQL server berhasil!");
        }
    }
    
    private static void createDatabase() throws SQLException {
        String createDbUrl = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + 
                           "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Jakarta";
        
        try (Connection conn = DriverManager.getConnection(createDbUrl, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {
            
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
            System.out.println("Database " + DB_NAME + " berhasil dibuat/sudah ada");
        }
    }
    
    private static void executeSQLScript() {
        String sql = "CREATE TABLE IF NOT EXISTS users (\n" + "    id INT AUTO_INCREMENT PRIMARY KEY,\n" + "    username VARCHAR(50) UNIQUE NOT NULL,\n" + "    password VARCHAR(255) NOT NULL,\n" + "    full_name VARCHAR(100) NOT NULL,\n" + "    role VARCHAR(20) DEFAULT 'admin',\n" + "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP\n" + ");\n" + "\n" + "CREATE TABLE IF NOT EXISTS customers (\n" + "    id INT AUTO_INCREMENT PRIMARY KEY,\n" + "    name VARCHAR(100) NOT NULL,\n" + "    phone VARCHAR(20),\n" + "    address TEXT,\n" + "    email VARCHAR(100),\n" + "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP\n" + ");\n" + "\n" + "CREATE TABLE IF NOT EXISTS service_packages (\n" + "    id INT AUTO_INCREMENT PRIMARY KEY,\n" + "    package_name VARCHAR(100) NOT NULL,\n" + "    description TEXT,\n" + "    price_per_kg DECIMAL(10,2) NOT NULL,\n" + "    duration_days INT DEFAULT 3,\n" + "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP\n" + ");\n" + "\n" + "CREATE TABLE IF NOT EXISTS laundry_orders (\n" + "    id INT AUTO_INCREMENT PRIMARY KEY,\n" + "    customer_id INT NOT NULL,\n" + "    package_id INT NOT NULL,\n" + "    weight_kg DECIMAL(5,2) NOT NULL,\n" + "    total_price DECIMAL(10,2) NOT NULL,\n" + "    status VARCHAR(20) DEFAULT 'pending',\n" + "    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n" + "    pickup_date TIMESTAMP NULL,\n" + "    notes TEXT,\n" + "    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE,\n" + "    FOREIGN KEY (package_id) REFERENCES service_packages(id) ON DELETE CASCADE\n" + ");\n" + "\n" + "CREATE TABLE IF NOT EXISTS payments (\n" + "    id INT AUTO_INCREMENT PRIMARY KEY,\n" + "    order_id INT NOT NULL,\n" + "    amount DECIMAL(10,2) NOT NULL,\n" + "    payment_method VARCHAR(20) DEFAULT 'cash',\n" + "    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n" + "    status VARCHAR(20) DEFAULT 'paid',\n" + "    FOREIGN KEY (order_id) REFERENCES laundry_orders(id) ON DELETE CASCADE\n" + ");\n";
        
        try (Connection conn = getConnection()) {
            String[] statements = sql.split(";");
            for (String statement : statements) {
                if (!statement.trim().isEmpty()) {
                    try (PreparedStatement pstmt = conn.prepareStatement(statement)) {
                        pstmt.execute();
                    }
                }
            }
            
            // Insert sample data
            insertSampleData(conn);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private static void insertSampleData(Connection conn) throws SQLException {
        // Insert default admin user
        String insertUser = "INSERT IGNORE INTO users (username, password, full_name, role) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertUser)) {
            pstmt.setString(1, "admin");
            pstmt.setString(2, "admin123");
            pstmt.setString(3, "Administrator");
            pstmt.setString(4, "admin");
            pstmt.executeUpdate();
        }
        
        // Insert sample service packages
        String insertPackage = "INSERT IGNORE INTO service_packages (package_name, description, price_per_kg, duration_days) VALUES (?, ?, ?, ?)";
        String[][] packages = {
            {"Cuci Kering", "Cuci dan kering biasa", "5000.00", "2"},
            {"Cuci Setrika", "Cuci, kering, dan setrika", "7000.00", "3"},
            {"Express", "Layanan kilat 1 hari", "10000.00", "1"},
            {"Dry Clean", "Cuci kering khusus", "15000.00", "3"}
        };
        
        try (PreparedStatement pstmt = conn.prepareStatement(insertPackage)) {
            for (String[] pkg : packages) {
                pstmt.setString(1, pkg[0]);
                pstmt.setString(2, pkg[1]);
                pstmt.setBigDecimal(3, new java.math.BigDecimal(pkg[2]));
                pstmt.setInt(4, Integer.parseInt(pkg[3]));
                pstmt.executeUpdate();
            }
        }
        
        // Insert sample customers
        String insertCustomer = "INSERT IGNORE INTO customers (name, phone, address, email) VALUES (?, ?, ?, ?)";
        String[][] customers = {
            {"John Doe", "081234567890", "Jl. Merdeka No. 123", "john@email.com"},
            {"Jane Smith", "081987654321", "Jl. Sudirman No. 456", "jane@email.com"},
            {"Budi Santoso", "081555666777", "Jl. Gatot Subroto No. 789", "budi@email.com"}
        };
        
        try (PreparedStatement pstmt = conn.prepareStatement(insertCustomer)) {
            for (String[] customer : customers) {
                pstmt.setString(1, customer[0]);
                pstmt.setString(2, customer[1]);
                pstmt.setString(3, customer[2]);
                pstmt.setString(4, customer[3]);
                pstmt.executeUpdate();
            }
        }
    }
    
    // Method untuk test koneksi database
    public static boolean testDatabaseConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
