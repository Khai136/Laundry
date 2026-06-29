package com.laundry.ui;

import com.laundry.dao.UserDAO;
import com.laundry.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;
import java.awt.*;
import java.util.List;

public class UserPanel extends JPanel {
    private UserDAO userDAO;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JTextField usernameField, fullNameField, passwordField;
    private JComboBox<String> roleCombo;
    private JButton saveButton, clearButton;
    
    public UserPanel() {
        userDAO = new UserDAO();
        initComponents();
        setupLayout();
        setupEventHandlers();
        loadUsers();
    }
    
    private void initComponents() {
        // Form fields
        usernameField = new JTextField(20);
        fullNameField = new JTextField(20);
        passwordField = new JTextField(20);
        
        String[] roles = {"bos", "karyawan"};
        roleCombo = new JComboBox<>(roles);
        
        // Buttons
        saveButton = new JButton("Tambah User");
        clearButton = new JButton("Clear");
        
        // Table
        String[] columns = {"ID", "Username", "Nama Lengkap", "Role"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        userTable = new JTable(tableModel);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel titleLabel = new JLabel("Manajemen User");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // Form Panel (Left)
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setPreferredSize(new Dimension(350, 0));
        formContainer.putClientProperty("FlatLaf.style", "arc: 20; background: darken(@background, 3%)");
        formContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Form Header
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel formTitle = new JLabel("Tambah User Baru");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(formTitle, gbc);
        
        gbc.gridwidth = 1;
        // Username
        gbc.gridy++; gbc.gridx = 0;
        formPanel.add(new JLabel("Username"), gbc);
        gbc.gridy++;
        usernameField.putClientProperty("JTextField.placeholderText", "Masukkan username");
        formPanel.add(usernameField, gbc);
        
        // Full Name
        gbc.gridy++;
        formPanel.add(new JLabel("Nama Lengkap"), gbc);
        gbc.gridy++;
        fullNameField.putClientProperty("JTextField.placeholderText", "Masukkan nama lengkap");
        formPanel.add(fullNameField, gbc);
        
        // Password
        gbc.gridy++;
        formPanel.add(new JLabel("Password"), gbc);
        gbc.gridy++;
        passwordField.putClientProperty("JTextField.placeholderText", "********");
        formPanel.add(passwordField, gbc);
        
        // Role
        gbc.gridy++;
        formPanel.add(new JLabel("Role"), gbc);
        gbc.gridy++;
        formPanel.add(roleCombo, gbc);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        buttonPanel.setOpaque(false);
        saveButton.putClientProperty("JButton.buttonType", "roundRect");
        saveButton.setBackground(new Color(52, 152, 219));
        saveButton.setForeground(Color.WHITE);
        
        clearButton.putClientProperty("JButton.buttonType", "roundRect");
        
        buttonPanel.add(saveButton);
        buttonPanel.add(clearButton);
        
        gbc.gridy++; gbc.weighty = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(20, 8, 8, 8);
        formPanel.add(buttonPanel, gbc);
        
        formContainer.add(formPanel, BorderLayout.NORTH);
        
        // Table Panel (Right)
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.putClientProperty("FlatLaf.style", "arc: 20; background: @background");
        tableContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);
        add(formContainer, BorderLayout.WEST);
        add(tableContainer, BorderLayout.CENTER);
    }
    
    private void setupEventHandlers() {
        saveButton.addActionListener(e -> saveUser());
        clearButton.addActionListener(e -> clearForm());
    }
    
    private void saveUser() {
        if (validateForm()) {
            User user = new User();
            user.setUsername(usernameField.getText().trim());
            user.setFullName(fullNameField.getText().trim());
            user.setPassword(passwordField.getText().trim());
            user.setRole((String) roleCombo.getSelectedItem());
            
            if (userDAO.save(user)) {
                JOptionPane.showMessageDialog(this, "User berhasil ditambahkan!");
                clearForm();
                loadUsers();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menambahkan user! Username mungkin sudah ada.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void clearForm() {
        usernameField.setText("");
        fullNameField.setText("");
        passwordField.setText("");
        roleCombo.setSelectedIndex(0);
    }
    
    private boolean validateForm() {
        if (usernameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username harus diisi!", "Validasi", JOptionPane.WARNING_MESSAGE);
            usernameField.requestFocus();
            return false;
        }
        
        if (fullNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama lengkap harus diisi!", "Validasi", JOptionPane.WARNING_MESSAGE);
            fullNameField.requestFocus();
            return false;
        }
        
        if (passwordField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password harus diisi!", "Validasi", JOptionPane.WARNING_MESSAGE);
            passwordField.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private void loadUsers() {
        tableModel.setRowCount(0);
        List<User> users = userDAO.findAll();
        
        for (User user : users) {
            Object[] row = {
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getRole()
            };
            tableModel.addRow(row);
        }
    }
}
