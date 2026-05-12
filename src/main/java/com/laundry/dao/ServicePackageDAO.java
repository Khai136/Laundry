package com.laundry.dao;

import com.laundry.database.DatabaseManager;
import com.laundry.model.ServicePackage;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicePackageDAO {
    
    public boolean save(ServicePackage servicePackage) {
        String sql = "INSERT INTO service_packages (package_name, description, price_per_kg, duration_days) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, servicePackage.getPackageName());
            pstmt.setString(2, servicePackage.getDescription());
            pstmt.setBigDecimal(3, servicePackage.getPricePerKg());
            pstmt.setInt(4, servicePackage.getDurationDays());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean update(ServicePackage servicePackage) {
        String sql = "UPDATE service_packages SET package_name = ?, description = ?, price_per_kg = ?, duration_days = ? WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, servicePackage.getPackageName());
            pstmt.setString(2, servicePackage.getDescription());
            pstmt.setBigDecimal(3, servicePackage.getPricePerKg());
            pstmt.setInt(4, servicePackage.getDurationDays());
            pstmt.setInt(5, servicePackage.getId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean delete(int id) {
        String sql = "DELETE FROM service_packages WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<ServicePackage> findAll() {
        List<ServicePackage> packages = new ArrayList<>();
        String sql = "SELECT * FROM service_packages ORDER BY package_name";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                ServicePackage pkg = new ServicePackage();
                pkg.setId(rs.getInt("id"));
                pkg.setPackageName(rs.getString("package_name"));
                pkg.setDescription(rs.getString("description"));
                pkg.setPricePerKg(rs.getBigDecimal("price_per_kg"));
                pkg.setDurationDays(rs.getInt("duration_days"));
                packages.add(pkg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return packages;
    }
    
    public ServicePackage findById(int id) {
        String sql = "SELECT * FROM service_packages WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                ServicePackage pkg = new ServicePackage();
                pkg.setId(rs.getInt("id"));
                pkg.setPackageName(rs.getString("package_name"));
                pkg.setDescription(rs.getString("description"));
                pkg.setPricePerKg(rs.getBigDecimal("price_per_kg"));
                pkg.setDurationDays(rs.getInt("duration_days"));
                return pkg;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
