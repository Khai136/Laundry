package com.laundry.ui;

import com.laundry.dao.UserDAO;
import com.laundry.database.DatabaseManager;
import com.laundry.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private UserDAO userDAO;
    private JLabel connectionStatusLabel;
    
    public LoginFrame() {
        userDAO = new UserDAO();
        initComponents();
        setupLayout();
        setupEventHandlers();
        checkDatabaseConnection();
    }
    
    private void initComponents() {
        setTitle("Login - Sistem Laundry (MySQL)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(false);
        
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        connectionStatusLabel = new JLabel("", JLabel.CENTER);
    }
    
    private void setupLayout() {
        setLayout(new GridBagLayout());
        getContentPane().setBackground(new Color(15, 23, 42)); // Deep background
        
        // --- LOGIN CARD ---
        JPanel loginCard = new JPanel(new GridBagLayout());
        loginCard.setPreferredSize(new Dimension(450, 550));
        loginCard.setBackground(new Color(30, 41, 59));
        loginCard.putClientProperty("FlatLaf.style", "arc: 40");
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 50, 10, 50);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // 1. Icon/Logo
        gbc.gridy = 0;
        JLabel logoIcon = new JLabel("L", JLabel.CENTER);
        logoIcon.setFont(new Font("Segoe UI", Font.BOLD, 72));
        logoIcon.setForeground(new Color(99, 102, 241));
        loginCard.add(logoIcon, gbc);
        
        // 2. Title
        gbc.gridy = 1;
        JLabel titleLabel = new JLabel("L-SOFT LOGIN", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        loginCard.add(titleLabel, gbc);
        
        gbc.gridy = 2;
        JLabel subtitleLabel = new JLabel("Silakan masuk untuk melanjutkan", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(148, 163, 184));
        loginCard.add(subtitleLabel, gbc);
        
        // 3. Fields
        gbc.gridy = 3; gbc.insets = new Insets(40, 50, 5, 50);
        JLabel userLbl = new JLabel("Username");
        userLbl.setForeground(new Color(203, 213, 225));
        loginCard.add(userLbl, gbc);
        
        gbc.gridy = 4; gbc.insets = new Insets(0, 50, 20, 50);
        usernameField.setPreferredSize(new Dimension(0, 45));
        usernameField.putClientProperty("JTextField.placeholderText", "admin");
        loginCard.add(usernameField, gbc);
        
        gbc.gridy = 5; gbc.insets = new Insets(0, 50, 5, 50);
        JLabel passLbl = new JLabel("Password");
        passLbl.setForeground(new Color(203, 213, 225));
        loginCard.add(passLbl, gbc);
        
        gbc.gridy = 6; gbc.insets = new Insets(0, 50, 30, 50);
        passwordField.setPreferredSize(new Dimension(0, 45));
        passwordField.putClientProperty("JTextField.placeholderText", "********");
        loginCard.add(passwordField, gbc);
        
        // 4. Login Button
        JButton loginButton = new JButton("LOGIN SEKARANG");
        loginButton.setPreferredSize(new Dimension(0, 50));
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setBackground(new Color(99, 102, 241));
        loginButton.setForeground(Color.WHITE);
        loginButton.putClientProperty("JButton.buttonType", "roundRect");
        
        gbc.gridy = 7; gbc.insets = new Insets(10, 50, 10, 50);
        loginCard.add(loginButton, gbc);
        
        // 5. Status
        gbc.gridy = 8;
        connectionStatusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        loginCard.add(connectionStatusLabel, gbc);
        
        add(loginCard);
        
        // Event handler
        loginButton.addActionListener(e -> performLogin());
        getRootPane().setDefaultButton(loginButton);
    }
    
    private void setupEventHandlers() {
        // Already handled in setupLayout
    }
    
    private void checkDatabaseConnection() {
        if (DatabaseManager.testDatabaseConnection()) {
            connectionStatusLabel.setText("✓ Database terhubung");
            connectionStatusLabel.setForeground(new Color(46, 204, 113));
        } else {
            connectionStatusLabel.setText("✗ Database tidak terhubung - Periksa XAMPP MySQL");
            connectionStatusLabel.setForeground(new Color(231, 76, 60));
        }
    }
    
    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username dan password harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Check database connection first
        if (!DatabaseManager.testDatabaseConnection()) {
            JOptionPane.showMessageDialog(this, 
                "Tidak dapat terhubung ke database!\n\n" +
                "Pastikan XAMPP MySQL sudah berjalan:\n" +
                "1. Buka XAMPP Control Panel\n" +
                "2. Start Apache & MySQL\n" +
                "3. Pastikan port 3306 tidak diblokir", 
                "Database Error", JOptionPane.ERROR_MESSAGE);
            checkDatabaseConnection();
            return;
        }
        
        User user = userDAO.authenticate(username, password);
        if (user != null) {
            JOptionPane.showMessageDialog(this, "Login berhasil! Selamat datang " + user.getFullName(), "Success", JOptionPane.INFORMATION_MESSAGE);
            
            // Open main dashboard
            SwingUtilities.invokeLater(() -> {
                new MainDashboard(user).setVisible(true);
                dispose();
            });
        } else {
            JOptionPane.showMessageDialog(this, "Username atau password salah!", "Error", JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
        }
    }
}
