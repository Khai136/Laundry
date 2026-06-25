package com.laundry.dao;

import com.laundry.database.DatabaseManager;
import com.laundry.model.Payment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {
    
    public boolean save(Payment payment) {
        String sql = "INSERT INTO payments (order_id, amount, payment_method) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, payment.getOrderId());
            pstmt.setBigDecimal(2, payment.getAmount());
            pstmt.setString(3, payment.getPaymentMethod());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Payment> findAll() {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM payments ORDER BY payment_date DESC";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Payment payment = new Payment();
                payment.setId(rs.getInt("id"));
                payment.setOrderId(rs.getInt("order_id"));
                payment.setAmount(rs.getBigDecimal("amount"));
                payment.setPaymentMethod(rs.getString("payment_method"));
                payment.setStatus(rs.getString("status"));
                
                Timestamp paymentDate = rs.getTimestamp("payment_date");
                if (paymentDate != null) {
                    payment.setPaymentDate(paymentDate.toLocalDateTime());
                }
                
                payments.add(payment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }
    
    public Payment findByOrderId(int orderId) {
        String sql = "SELECT * FROM payments WHERE order_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Payment payment = new Payment();
                payment.setId(rs.getInt("id"));
                payment.setOrderId(rs.getInt("order_id"));
                payment.setAmount(rs.getBigDecimal("amount"));
                payment.setPaymentMethod(rs.getString("payment_method"));
                payment.setStatus(rs.getString("status"));
                
                Timestamp paymentDate = rs.getTimestamp("payment_date");
                if (paymentDate != null) {
                    payment.setPaymentDate(paymentDate.toLocalDateTime());
                }
                
                return payment;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public java.math.BigDecimal getTodayRevenue() {
        String sql = "SELECT SUM(amount) FROM payments WHERE DATE(payment_date) = CURDATE()";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                java.math.BigDecimal val = rs.getBigDecimal(1);
                return val != null ? val : java.math.BigDecimal.ZERO;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return java.math.BigDecimal.ZERO;
    }
    
    public List<Object[]> getWeeklyRevenue() {
        List<Object[]> data = new ArrayList<>();
        String sql = "SELECT DATE(payment_date) as p_date, SUM(amount) as total " +
                     "FROM payments " +
                     "WHERE payment_date >= DATE_SUB(CURDATE(), INTERVAL 6 DAY) " +
                     "GROUP BY DATE(payment_date) " +
                     "ORDER BY p_date ASC";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                data.add(new Object[]{rs.getDate("p_date"), rs.getBigDecimal("total")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
}

