package com.laundry.dao;

import com.laundry.database.DatabaseManager;
import com.laundry.model.LaundryOrder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LaundryOrderDAO {
    
    public boolean save(LaundryOrder order) {
        String sql = "INSERT INTO laundry_orders (customer_id, package_id, weight_kg, total_price, notes) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, order.getCustomerId());
            pstmt.setInt(2, order.getPackageId());
            pstmt.setBigDecimal(3, order.getWeightKg());
            pstmt.setBigDecimal(4, order.getTotalPrice());
            pstmt.setString(5, order.getNotes());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateStatus(int orderId, String status) {
        String sql = "UPDATE laundry_orders SET status = ? WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, orderId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<LaundryOrder> findAll() {
        List<LaundryOrder> orders = new ArrayList<>();
        String sql = "SELECT lo.*, c.name as customer_name, sp.package_name\n" + "FROM laundry_orders lo\n" + "JOIN customers c ON lo.customer_id = c.id\n" + "JOIN service_packages sp ON lo.package_id = sp.id\n" + "ORDER BY lo.order_date DESC\n";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                LaundryOrder order = new LaundryOrder();
                order.setId(rs.getInt("id"));
                order.setCustomerId(rs.getInt("customer_id"));
                order.setPackageId(rs.getInt("package_id"));
                order.setWeightKg(rs.getBigDecimal("weight_kg"));
                order.setTotalPrice(rs.getBigDecimal("total_price"));
                order.setStatus(rs.getString("status"));
                order.setNotes(rs.getString("notes"));
                order.setCustomerName(rs.getString("customer_name"));
                order.setPackageName(rs.getString("package_name"));
                
                Timestamp orderDate = rs.getTimestamp("order_date");
                if (orderDate != null) {
                    order.setOrderDate(orderDate.toLocalDateTime());
                }
                
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
    
    public LaundryOrder findById(int id) {
        String sql = "SELECT lo.*, c.name as customer_name, sp.package_name FROM laundry_orders lo JOIN customers c ON lo.customer_id = c.id JOIN service_packages sp ON lo.package_id = sp.id WHERE lo.id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                LaundryOrder order = new LaundryOrder();
                order.setId(rs.getInt("id"));
                order.setCustomerId(rs.getInt("customer_id"));
                order.setPackageId(rs.getInt("package_id"));
                order.setWeightKg(rs.getBigDecimal("weight_kg"));
                order.setTotalPrice(rs.getBigDecimal("total_price"));
                order.setStatus(rs.getString("status"));
                order.setNotes(rs.getString("notes"));
                order.setCustomerName(rs.getString("customer_name"));
                order.setPackageName(rs.getString("package_name"));
                
                Timestamp orderDate = rs.getTimestamp("order_date");
                if (orderDate != null) {
                    order.setOrderDate(orderDate.toLocalDateTime());
                }
                
                return order;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
