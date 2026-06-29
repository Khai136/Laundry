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
            com.formdev.flatlaf.themes.FlatMacLightLaf.setup();

            // --- PREMIUM GLOBAL STYLING (Light Theme) ---

            // Colors — indigo accent on white base
            UIManager.put("Component.accentColor", new java.awt.Color(99, 102, 241));
            UIManager.put("Component.focusColor",  new java.awt.Color(129, 140, 248));
            UIManager.put("Selection.background",  new java.awt.Color(79,  70, 229));
            UIManager.put("Selection.foreground",  new java.awt.Color(255, 255, 255));

            // Global background
            UIManager.put("Panel.background",      new java.awt.Color(245, 247, 250));
            UIManager.put("control",               new java.awt.Color(245, 247, 250));

            // Arcs (Rounded Corners)
            UIManager.put("Button.arc",            14);
            UIManager.put("Component.arc",         12);
            UIManager.put("TextComponent.arc",     10);
            UIManager.put("CheckBox.arc",          6);
            UIManager.put("ProgressBar.arc",       12);

            // Fonts
            UIManager.put("defaultFont", new Font("Segoe UI", Font.PLAIN, 13));

            // Table
            UIManager.put("Table.intercellSpacing",   new Dimension(0, 0));
            UIManager.put("Table.rowHeight",           40);
            UIManager.put("TableHeader.height",        45);
            UIManager.put("TableHeader.background",    new java.awt.Color(241, 245, 249));
            UIManager.put("TableHeader.foreground",    new java.awt.Color(15,  23,  42));
            UIManager.put("Table.alternateRowColor",   new java.awt.Color(248, 250, 252));
            UIManager.put("Table.showHorizontalLines", true);
            UIManager.put("Table.gridColor",           new java.awt.Color(226, 232, 240));
            UIManager.put("Table.background",          new java.awt.Color(255, 255, 255));
            UIManager.put("Table.foreground",          new java.awt.Color(15,  23,  42));

            // ScrollBar
            UIManager.put("ScrollBar.thumbArc",    999);
            UIManager.put("ScrollBar.thumbInsets", new Insets(2, 2, 2, 2));
            UIManager.put("ScrollBar.thumb",       new java.awt.Color(203, 213, 225));
            UIManager.put("ScrollBar.track",       new java.awt.Color(248, 250, 252));

            // Menu
            UIManager.put("MenuItem.selectionBackground", new java.awt.Color(79, 70, 229));
            UIManager.put("Menu.background",             new java.awt.Color(255, 255, 255));
            
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
