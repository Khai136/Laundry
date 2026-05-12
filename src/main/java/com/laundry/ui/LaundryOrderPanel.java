package com.laundry.ui;

import com.laundry.dao.CustomerDAO;
import com.laundry.dao.LaundryOrderDAO;
import com.laundry.dao.ServicePackageDAO;
import com.laundry.model.Customer;
import com.laundry.model.LaundryOrder;
import com.laundry.model.ServicePackage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class LaundryOrderPanel extends JPanel {
    private LaundryOrderDAO orderDAO;
    private CustomerDAO customerDAO;
    private ServicePackageDAO packageDAO;
    private JTable orderTable;
    private DefaultTableModel tableModel;
    private JComboBox<Customer> customerCombo;
    private JComboBox<ServicePackage> packageCombo;
    private JTextField weightField, totalPriceField;
    private JTextArea notesArea;
    private JComboBox<String> statusCombo;
    private JButton saveButton, updateStatusButton, clearButton;
    private LaundryOrder selectedOrder;
    
    public LaundryOrderPanel() {
        orderDAO = new LaundryOrderDAO();
        customerDAO = new CustomerDAO();
        packageDAO = new ServicePackageDAO();
        initComponents();
        setupLayout();
        setupEventHandlers();
        loadData();
    }
    
    private void initComponents() {
        // Form fields
        customerCombo = new JComboBox<>();
        packageCombo = new JComboBox<>();
        weightField = new JTextField(15);
        totalPriceField = new JTextField(15);
        totalPriceField.setEditable(false);
        notesArea = new JTextArea(3, 15);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        
        String[] statuses = {"pending", "processing", "ready", "completed"};
        statusCombo = new JComboBox<>(statuses);
        
        // Buttons
        saveButton = new JButton("Simpan Order");
        updateStatusButton = new JButton("Update Status");
        clearButton = new JButton("Clear");
        
        // Table
        String[] columns = {"ID", "Pelanggan", "Paket", "Berat (Kg)", "Total", "Status", "Tanggal", "Catatan"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        orderTable = new JTable(tableModel);
        orderTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel titleLabel = new JLabel("Cucian Masuk");
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
        JLabel formTitle = new JLabel("Form Order Baru");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(formTitle, gbc);
        
        gbc.gridwidth = 1;
        // Customer
        gbc.gridy++; gbc.gridx = 0;
        formPanel.add(new JLabel("Pelanggan"), gbc);
        gbc.gridy++;
        formPanel.add(customerCombo, gbc);
        
        // Package
        gbc.gridy++;
        formPanel.add(new JLabel("Paket Layanan"), gbc);
        gbc.gridy++;
        formPanel.add(packageCombo, gbc);
        
        // Weight & Total Price (Side by Side)
        JPanel rowPanel = new JPanel(new GridLayout(2, 2, 10, 5));
        rowPanel.setOpaque(false);
        rowPanel.add(new JLabel("Berat (Kg)"));
        rowPanel.add(new JLabel("Total Harga"));
        weightField.putClientProperty("JTextField.placeholderText", "0.0");
        rowPanel.add(weightField);
        totalPriceField.putClientProperty("FlatLaf.style", "background: darken(@background, 5%)");
        rowPanel.add(totalPriceField);
        
        gbc.gridy++;
        formPanel.add(rowPanel, gbc);
        
        // Status
        gbc.gridy++;
        formPanel.add(new JLabel("Status"), gbc);
        gbc.gridy++;
        formPanel.add(statusCombo, gbc);
        
        // Notes
        gbc.gridy++;
        formPanel.add(new JLabel("Catatan"), gbc);
        gbc.gridy++;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        JScrollPane notesScroll = new JScrollPane(notesArea);
        notesScroll.setPreferredSize(new Dimension(0, 60));
        formPanel.add(notesScroll, gbc);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonPanel.setOpaque(false);
        saveButton.putClientProperty("JButton.buttonType", "roundRect");
        saveButton.setBackground(new Color(46, 204, 113));
        saveButton.setForeground(Color.WHITE);
        
        updateStatusButton.putClientProperty("JButton.buttonType", "roundRect");
        clearButton.putClientProperty("JButton.buttonType", "roundRect");
        
        buttonPanel.add(saveButton);
        buttonPanel.add(updateStatusButton);
        buttonPanel.add(clearButton);
        
        gbc.gridy++; gbc.weighty = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(20, 8, 8, 8);
        formPanel.add(buttonPanel, gbc);
        
        formContainer.add(formPanel, BorderLayout.NORTH);
        
        // Table Panel (Right)
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.putClientProperty("FlatLaf.style", "arc: 20; background: @background");
        tableContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(orderTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);
        add(formContainer, BorderLayout.WEST);
        add(tableContainer, BorderLayout.CENTER);
    }
    
    private void setupEventHandlers() {
        saveButton.addActionListener(e -> saveOrder());
        updateStatusButton.addActionListener(e -> updateOrderStatus());
        clearButton.addActionListener(e -> clearForm());
        
        // Calculate total price when weight or package changes
        weightField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                calculateTotalPrice();
            }
        });
        
        packageCombo.addActionListener(e -> calculateTotalPrice());
        
        orderTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectOrder();
            }
        });
    }
    
    private void saveOrder() {
        if (validateForm()) {
            LaundryOrder order = new LaundryOrder();
            Customer customer = (Customer) customerCombo.getSelectedItem();
            ServicePackage servicePackage = (ServicePackage) packageCombo.getSelectedItem();
            
            order.setCustomerId(customer.getId());
            order.setPackageId(servicePackage.getId());
            order.setWeightKg(new BigDecimal(weightField.getText().trim()));
            order.setTotalPrice(new BigDecimal(totalPriceField.getText().replace("Rp ", "").replace(",", "")));
            order.setNotes(notesArea.getText().trim());
            
            if (orderDAO.save(order)) {
                JOptionPane.showMessageDialog(this, "Order berhasil disimpan!");
                clearForm();
                loadOrders();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan order!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void updateOrderStatus() {
        if (selectedOrder != null) {
            String newStatus = (String) statusCombo.getSelectedItem();
            if (orderDAO.updateStatus(selectedOrder.getId(), newStatus)) {
                JOptionPane.showMessageDialog(this, "Status order berhasil diupdate!");
                clearForm();
                loadOrders();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal mengupdate status!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void clearForm() {
        customerCombo.setSelectedIndex(-1);
        packageCombo.setSelectedIndex(-1);
        weightField.setText("");
        totalPriceField.setText("");
        notesArea.setText("");
        statusCombo.setSelectedIndex(0);
        selectedOrder = null;
        orderTable.clearSelection();
    }
    
    private void selectOrder() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow >= 0) {
            int orderId = (Integer) tableModel.getValueAt(selectedRow, 0);
            selectedOrder = orderDAO.findById(orderId);
            
            if (selectedOrder != null) {
                // Find and select customer
                for (int i = 0; i < customerCombo.getItemCount(); i++) {
                    Customer customer = customerCombo.getItemAt(i);
                    if (customer.getId() == selectedOrder.getCustomerId()) {
                        customerCombo.setSelectedIndex(i);
                        break;
                    }
                }
                
                // Find and select package
                for (int i = 0; i < packageCombo.getItemCount(); i++) {
                    ServicePackage pkg = packageCombo.getItemAt(i);
                    if (pkg.getId() == selectedOrder.getPackageId()) {
                        packageCombo.setSelectedIndex(i);
                        break;
                    }
                }
                
                weightField.setText(selectedOrder.getWeightKg().toString());
                totalPriceField.setText("Rp " + selectedOrder.getTotalPrice());
                notesArea.setText(selectedOrder.getNotes());
                statusCombo.setSelectedItem(selectedOrder.getStatus());
            }
        }
    }
    
    private void calculateTotalPrice() {
        try {
            if (!weightField.getText().trim().isEmpty() && packageCombo.getSelectedItem() != null) {
                BigDecimal weight = new BigDecimal(weightField.getText().trim());
                ServicePackage selectedPackage = (ServicePackage) packageCombo.getSelectedItem();
                BigDecimal totalPrice = weight.multiply(selectedPackage.getPricePerKg());
                totalPriceField.setText("Rp " + totalPrice);
            }
        } catch (NumberFormatException e) {
            totalPriceField.setText("");
        }
    }
    
    private boolean validateForm() {
        if (customerCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Pilih pelanggan!", "Validasi", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        if (packageCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Pilih paket layanan!", "Validasi", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        try {
            BigDecimal weight = new BigDecimal(weightField.getText().trim());
            if (weight.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this, "Berat harus lebih dari 0!", "Validasi", JOptionPane.WARNING_MESSAGE);
                weightField.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Berat harus berupa angka!", "Validasi", JOptionPane.WARNING_MESSAGE);
            weightField.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private void loadData() {
        loadCustomers();
        loadPackages();
        loadOrders();
    }
    
    private void loadCustomers() {
        customerCombo.removeAllItems();
        List<Customer> customers = customerDAO.findAll();
        for (Customer customer : customers) {
            customerCombo.addItem(customer);
        }
    }
    
    private void loadPackages() {
        packageCombo.removeAllItems();
        List<ServicePackage> packages = packageDAO.findAll();
        for (ServicePackage pkg : packages) {
            packageCombo.addItem(pkg);
        }
    }
    
    private void loadOrders() {
        tableModel.setRowCount(0);
        List<LaundryOrder> orders = orderDAO.findAll();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        for (LaundryOrder order : orders) {
            Object[] row = {
                order.getId(),
                order.getCustomerName(),
                order.getPackageName(),
                order.getWeightKg() + " kg",
                "Rp " + order.getTotalPrice(),
                order.getStatus(),
                order.getOrderDate() != null ? order.getOrderDate().format(formatter) : "",
                order.getNotes()
            };
            tableModel.addRow(row);
        }
    }
}
