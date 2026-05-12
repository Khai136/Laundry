package com.laundry;

import com.formdev.flatlaf.FlatLightLaf;
import com.laundry.database.DatabaseManager;
import com.laundry.ui.LoginFrame;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Font;

public class Main {
    public static void main(String[] args) {
        // Set Look and Feel
        try {
            com.formdev.flatlaf.themes.FlatMacDarkLaf.setup();
            
            // --- PREMIUM GLOBAL STYLING ---
            
            // Colors
            UIManager.put("Component.accentColor", "#6366f1"); // Modern Indigo
            UIManager.put("Component.focusColor", "#818cf8");
            UIManager.put("Selection.background", "#4f46e5");
            UIManager.put("Selection.foreground", "#ffffff");
            
            // Arcs (Rounded Corners)
            UIManager.put("Button.arc", 12);
            UIManager.put("Component.arc", 12);
            UIManager.put("TextComponent.arc", 12);
            UIManager.put("CheckBox.arc", 4);
            UIManager.put("ProgressBar.arc", 12);
            
            // Fonts
            UIManager.put("defaultFont", new Font("Segoe UI Semibold", Font.PLAIN, 13));
            
            // Table
            UIManager.put("Table.intercellSpacing", new Dimension(0, 0));
            UIManager.put("Table.rowHeight", 40);
            UIManager.put("TableHeader.height", 45);
            UIManager.put("TableHeader.background", "#1e1e2e");
            UIManager.put("Table.alternateRowColor", "#242435");
            UIManager.put("Table.showHorizontalLines", true);
            UIManager.put("Table.gridColor", "#2d2d3f");
            
            // ScrollBar
            UIManager.put("ScrollBar.thumbArc", 999);
            UIManager.put("ScrollBar.thumbInsets", new Insets(2, 2, 2, 2));
            UIManager.put("ScrollBar.track", "#1a1a27");
            
            // Menu
            UIManager.put("MenuItem.selectionBackground", "#4f46e5");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Show splash screen
        showSplashScreen();
        
        // Initialize database
        DatabaseManager.initializeDatabase();
        
        // Test database connection
        if (!DatabaseManager.testDatabaseConnection()) {
            JOptionPane.showMessageDialog(null, 
                "Tidak dapat terhubung ke database MySQL!\n\n" +
                "Pastikan:\n" +
                "1. XAMPP sudah terinstall\n" +
                "2. MySQL service sudah berjalan\n" +
                "3. Port 3306 tidak diblokir\n\n" +
                "Buka XAMPP Control Panel dan start MySQL service.",
                "Database Connection Error", 
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        
        // Start application
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
    
    private static void showSplashScreen() {
        JWindow splash = new JWindow();
        splash.setSize(400, 200);
        splash.setLocationRelativeTo(null);
        
        JPanel panel = new JPanel();
        panel.setBackground(new java.awt.Color(52, 152, 219));
        panel.setLayout(new java.awt.BorderLayout());
        
        JLabel titleLabel = new JLabel("SISTEM LAUNDRY", JLabel.CENTER);
        titleLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 24));
        titleLabel.setForeground(java.awt.Color.WHITE);
        
        JLabel statusLabel = new JLabel("Menginisialisasi database...", JLabel.CENTER);
        statusLabel.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 12));
        statusLabel.setForeground(java.awt.Color.WHITE);
        
        panel.add(titleLabel, java.awt.BorderLayout.CENTER);
        panel.add(statusLabel, java.awt.BorderLayout.SOUTH);
        
        splash.add(panel);
        splash.setVisible(true);
        
        // Show splash for 2 seconds
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        splash.dispose();
    }
}
