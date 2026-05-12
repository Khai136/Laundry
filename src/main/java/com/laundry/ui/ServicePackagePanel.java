package com.laundry.ui;

import com.laundry.dao.ServicePackageDAO;
import com.laundry.model.ServicePackage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class ServicePackagePanel extends JPanel {
    private ServicePackageDAO servicePackageDAO;
    private JTable packageTable;
    private DefaultTableModel tableModel;
    private JTextField nameField, priceField, durationField;
    private JTextArea descriptionArea;
    private JButton saveButton, updateButton, deleteButton, clearButton;
    private ServicePackage selectedPackage;
    
    public ServicePackagePanel() {
        servicePackageDAO = new ServicePackageDAO();
        initComponents();
        setupLayout();
        setupEventHandlers();
        loadPackages();
    }
    
    private void initComponents() {
        // Form fields
        nameField = new JTextField(20);
        priceField = new JTextField(20);
        durationField = new JTextField(20);
        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        
        // Buttons
        saveButton = new JButton("Simpan");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Hapus");
        clearButton = new JButton("Clear");
        
        // Table
        String[] columns = {"ID", "Nama Paket", "Harga/Kg", "Durasi (Hari)", "Deskripsi"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        packageTable = new JTable(tableModel);
        packageTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel titleLabel = new JLabel("Paket Layanan");
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
        JLabel formTitle = new JLabel("Detail Paket");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(formTitle, gbc);
        
        gbc.gridwidth = 1;
        // Package Name
        gbc.gridy++; gbc.gridx = 0;
        formPanel.add(new JLabel("Nama Paket"), gbc);
        gbc.gridy++;
        nameField.putClientProperty("JTextField.placeholderText", "Contoh: Cuci Kering");
        formPanel.add(nameField, gbc);
        
        // Price & Duration (Side by Side)
        JPanel rowPanel = new JPanel(new GridLayout(2, 2, 10, 5));
        rowPanel.setOpaque(false);
        rowPanel.add(new JLabel("Harga/Kg"));
        rowPanel.add(new JLabel("Durasi (Hari)"));
        priceField.putClientProperty("JTextField.placeholderText", "0");
        rowPanel.add(priceField);
        durationField.putClientProperty("JTextField.placeholderText", "0");
        rowPanel.add(durationField);
        
        gbc.gridy++;
        formPanel.add(rowPanel, gbc);
        
        // Description
        gbc.gridy++;
        formPanel.add(new JLabel("Deskripsi"), gbc);
        gbc.gridy++;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        JScrollPane descScroll = new JScrollPane(descriptionArea);
        descScroll.setPreferredSize(new Dimension(0, 80));
        formPanel.add(descScroll, gbc);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonPanel.setOpaque(false);
        saveButton.putClientProperty("JButton.buttonType", "roundRect");
        saveButton.setBackground(new Color(46, 204, 113));
        saveButton.setForeground(Color.WHITE);
        
        updateButton.putClientProperty("JButton.buttonType", "roundRect");
        deleteButton.putClientProperty("JButton.buttonType", "roundRect");
        clearButton.putClientProperty("JButton.buttonType", "roundRect");
        
        buttonPanel.add(saveButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        
        gbc.gridy++; gbc.weighty = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(20, 8, 8, 8);
        formPanel.add(buttonPanel, gbc);
        
        formContainer.add(formPanel, BorderLayout.NORTH);
        
        // Table Panel (Right)
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.putClientProperty("FlatLaf.style", "arc: 20; background: @background");
        tableContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(packageTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);
        add(formContainer, BorderLayout.WEST);
        add(tableContainer, BorderLayout.CENTER);
    }
    
    private void setupEventHandlers() {
        saveButton.addActionListener(e -> savePackage());
        updateButton.addActionListener(e -> updatePackage());
        deleteButton.addActionListener(e -> deletePackage());
        clearButton.addActionListener(e -> clearForm());
        
        packageTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectPackage();
            }
        });
    }
    
    private void savePackage() {
        if (validateForm()) {
            ServicePackage servicePackage = new ServicePackage();
            servicePackage.setPackageName(nameField.getText().trim());
            servicePackage.setDescription(descriptionArea.getText().trim());
            servicePackage.setPricePerKg(new BigDecimal(priceField.getText().trim()));
            servicePackage.setDurationDays(Integer.parseInt(durationField.getText().trim()));
            
            if (servicePackageDAO.save(servicePackage)) {
                JOptionPane.showMessageDialog(this, "Paket layanan berhasil disimpan!");
                clearForm();
                loadPackages();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan paket layanan!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void updatePackage() {
        if (selectedPackage != null && validateForm()) {
            selectedPackage.setPackageName(nameField.getText().trim());
            selectedPackage.setDescription(descriptionArea.getText().trim());
            selectedPackage.setPricePerKg(new BigDecimal(priceField.getText().trim()));
            selectedPackage.setDurationDays(Integer.parseInt(durationField.getText().trim()));
            
            if (servicePackageDAO.update(selectedPackage)) {
                JOptionPane.showMessageDialog(this, "Paket layanan berhasil diupdate!");
                clearForm();
                loadPackages();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal mengupdate paket layanan!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void deletePackage() {
        if (selectedPackage != null) {
            int result = JOptionPane.showConfirmDialog(this, 
                "Apakah Anda yakin ingin menghapus paket " + selectedPackage.getPackageName() + "?", 
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
            
            if (result == JOptionPane.YES_OPTION) {
                if (servicePackageDAO.delete(selectedPackage.getId())) {
                    JOptionPane.showMessageDialog(this, "Paket layanan berhasil dihapus!");
                    clearForm();
                    loadPackages();
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal menghapus paket layanan!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private void clearForm() {
        nameField.setText("");
        priceField.setText("");
        durationField.setText("");
        descriptionArea.setText("");
        selectedPackage = null;
        packageTable.clearSelection();
    }
    
    private void selectPackage() {
        int selectedRow = packageTable.getSelectedRow();
        if (selectedRow >= 0) {
            int packageId = (Integer) tableModel.getValueAt(selectedRow, 0);
            selectedPackage = servicePackageDAO.findById(packageId);
            
            if (selectedPackage != null) {
                nameField.setText(selectedPackage.getPackageName());
                priceField.setText(selectedPackage.getPricePerKg().toString());
                durationField.setText(String.valueOf(selectedPackage.getDurationDays()));
                descriptionArea.setText(selectedPackage.getDescription());
            }
        }
    }
    
    private boolean validateForm() {
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama paket harus diisi!", "Validasi", JOptionPane.WARNING_MESSAGE);
            nameField.requestFocus();
            return false;
        }
        
        try {
            BigDecimal price = new BigDecimal(priceField.getText().trim());
            if (price.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this, "Harga harus lebih dari 0!", "Validasi", JOptionPane.WARNING_MESSAGE);
                priceField.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harga harus berupa angka!", "Validasi", JOptionPane.WARNING_MESSAGE);
            priceField.requestFocus();
            return false;
        }
        
        try {
            int duration = Integer.parseInt(durationField.getText().trim());
            if (duration <= 0) {
                JOptionPane.showMessageDialog(this, "Durasi harus lebih dari 0!", "Validasi", JOptionPane.WARNING_MESSAGE);
                durationField.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Durasi harus berupa angka!", "Validasi", JOptionPane.WARNING_MESSAGE);
            durationField.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private void loadPackages() {
        tableModel.setRowCount(0);
        List<ServicePackage> packages = servicePackageDAO.findAll();
        
        for (ServicePackage pkg : packages) {
            Object[] row = {
                pkg.getId(),
                pkg.getPackageName(),
                "Rp " + pkg.getPricePerKg(),
                pkg.getDurationDays(),
                pkg.getDescription()
            };
            tableModel.addRow(row);
        }
    }
}
