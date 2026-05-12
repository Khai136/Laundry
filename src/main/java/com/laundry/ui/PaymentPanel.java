package com.laundry.ui;

import com.laundry.dao.LaundryOrderDAO;
import com.laundry.dao.PaymentDAO;
import com.laundry.model.LaundryOrder;
import com.laundry.model.Payment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PaymentPanel extends JPanel {
    private PaymentDAO paymentDAO;
    private LaundryOrderDAO orderDAO;
    private JTable paymentTable;
    private DefaultTableModel tableModel;
    private JComboBox<LaundryOrder> orderCombo;
    private JTextField amountField;
    private JComboBox<String> methodCombo;
    private JButton saveButton, clearButton;
    
    public PaymentPanel() {
        paymentDAO = new PaymentDAO();
        orderDAO = new LaundryOrderDAO();
        initComponents();
        setupLayout();
        setupEventHandlers();
        loadData();
    }
    
    private void initComponents() {
        // Form fields
        orderCombo = new JComboBox<>();
        amountField = new JTextField(15);
        
        String[] methods = {"cash", "transfer", "debit", "credit"};
        methodCombo = new JComboBox<>(methods);
        
        // Buttons
        saveButton = new JButton("Proses Pembayaran");
        clearButton = new JButton("Clear");
        
        // Table
        String[] columns = {"ID", "Order ID", "Pelanggan", "Jumlah", "Metode", "Tanggal", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        paymentTable = new JTable(tableModel);
        paymentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel titleLabel = new JLabel("Transaksi Pembayaran");
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
        JLabel formTitle = new JLabel("Detail Pembayaran");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(formTitle, gbc);
        
        gbc.gridwidth = 1;
        // Order
        gbc.gridy++; gbc.gridx = 0;
        formPanel.add(new JLabel("Pilih Order"), gbc);
        gbc.gridy++;
        formPanel.add(orderCombo, gbc);
        
        // Amount
        gbc.gridy++;
        formPanel.add(new JLabel("Jumlah Pembayaran"), gbc);
        gbc.gridy++;
        amountField.putClientProperty("JTextField.placeholderText", "0.00");
        formPanel.add(amountField, gbc);
        
        // Payment Method
        gbc.gridy++;
        formPanel.add(new JLabel("Metode Pembayaran"), gbc);
        gbc.gridy++;
        formPanel.add(methodCombo, gbc);
        
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
        
        JScrollPane scrollPane = new JScrollPane(paymentTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);
        add(formContainer, BorderLayout.WEST);
        add(tableContainer, BorderLayout.CENTER);
    }
    
    private void setupEventHandlers() {
        saveButton.addActionListener(e -> processPayment());
        clearButton.addActionListener(e -> clearForm());
        
        orderCombo.addActionListener(e -> {
            LaundryOrder selectedOrder = (LaundryOrder) orderCombo.getSelectedItem();
            if (selectedOrder != null) {
                amountField.setText(selectedOrder.getTotalPrice().toString());
            }
        });
    }
    
    private void processPayment() {
        if (validateForm()) {
            LaundryOrder order = (LaundryOrder) orderCombo.getSelectedItem();
            
            Payment payment = new Payment();
            payment.setOrderId(order.getId());
            payment.setAmount(new BigDecimal(amountField.getText().trim()));
            payment.setPaymentMethod((String) methodCombo.getSelectedItem());
            
            if (paymentDAO.save(payment)) {
                // Update order status to completed
                orderDAO.updateStatus(order.getId(), "completed");
                
                JOptionPane.showMessageDialog(this, "Pembayaran berhasil diproses!");
                clearForm();
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal memproses pembayaran!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void clearForm() {
        orderCombo.setSelectedIndex(-1);
        amountField.setText("");
        methodCombo.setSelectedIndex(0);
    }
    
    private boolean validateForm() {
        if (orderCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Pilih order!", "Validasi", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        try {
            BigDecimal amount = new BigDecimal(amountField.getText().trim());
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this, "Jumlah harus lebih dari 0!", "Validasi", JOptionPane.WARNING_MESSAGE);
                amountField.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Jumlah harus berupa angka!", "Validasi", JOptionPane.WARNING_MESSAGE);
            amountField.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private void loadData() {
        loadUnpaidOrders();
        loadPayments();
    }
    
    private void loadUnpaidOrders() {
        orderCombo.removeAllItems();
        List<LaundryOrder> orders = orderDAO.findAll();
        
        for (LaundryOrder order : orders) {
            // Only show orders that haven't been paid
            if (paymentDAO.findByOrderId(order.getId()) == null) {
                orderCombo.addItem(order);
            }
        }
    }
    
    private void loadPayments() {
        tableModel.setRowCount(0);
        List<Payment> payments = paymentDAO.findAll();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        for (Payment payment : payments) {
            LaundryOrder order = orderDAO.findById(payment.getOrderId());
            Object[] row = {
                payment.getId(),
                payment.getOrderId(),
                order != null ? order.getCustomerName() : "N/A",
                "Rp " + payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getPaymentDate() != null ? payment.getPaymentDate().format(formatter) : "",
                payment.getStatus()
            };
            tableModel.addRow(row);
        }
    }
}
