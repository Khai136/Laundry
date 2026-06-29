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

public class ReportPanel extends JPanel {
    private LaundryOrderDAO orderDAO;
    private PaymentDAO paymentDAO;
    private JTable reportTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> reportTypeCombo;
    private JButton generateButton;
    private JButton exportButton;
    private JLabel totalOrdersLabel, totalRevenueLabel, pendingOrdersLabel;
    
    public ReportPanel() {
        orderDAO = new LaundryOrderDAO();
        paymentDAO = new PaymentDAO();
        initComponents();
        setupLayout();
        setupEventHandlers();
        loadSummary();
        generateOrderReport();
    }
    
    private void initComponents() {
        // Report type combo
        String[] reportTypes = {"Semua Order", "Order Pending", "Order Selesai", "Riwayat Pembayaran"};
        reportTypeCombo = new JComboBox<>(reportTypes);
        
        // Generate button
        generateButton = new JButton("Generate Report");
        
        // Export button
        exportButton = new JButton("Export CSV");
        
        // Summary labels
        totalOrdersLabel = new JLabel("Total Orders: 0");
        totalRevenueLabel = new JLabel("Total Revenue: Rp 0");
        pendingOrdersLabel = new JLabel("Pending Orders: 0");
        
        // Table
        String[] columns = {"ID", "Tanggal", "Pelanggan", "Paket", "Berat", "Total", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        reportTable = new JTable(tableModel);
        reportTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(20, 20));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel titleLabel = new JLabel("Laporan & Statistik");
        titleLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 28));
        titleLabel.setForeground(new Color(15, 23, 42));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // Control Panel (Filter)
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        controlPanel.setOpaque(false);
        controlPanel.add(new JLabel("Jenis Laporan:"));
        controlPanel.add(reportTypeCombo);
        generateButton.putClientProperty("JButton.buttonType", "roundRect");
        generateButton.setBackground(new Color(99, 102, 241));
        generateButton.setForeground(Color.WHITE);
        controlPanel.add(generateButton);
        
        exportButton.putClientProperty("JButton.buttonType", "roundRect");
        exportButton.setBackground(new Color(34, 197, 94));
        exportButton.setForeground(Color.WHITE);
        controlPanel.add(exportButton);
        
        // Summary Panel (Dashboard Cards)
        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 25, 0));
        summaryPanel.setOpaque(false);
        summaryPanel.setPreferredSize(new Dimension(0, 120));
        
        summaryPanel.add(createSummaryCard("TOTAL ORDER", totalOrdersLabel, new Color(99, 102, 241)));
        summaryPanel.add(createSummaryCard("PENDAPATAN", totalRevenueLabel, new Color(34, 197, 94)));
        summaryPanel.add(createSummaryCard("PENDING", pendingOrdersLabel, new Color(249, 115, 22)));
        
        // Table Panel
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(Color.WHITE);
        tableContainer.putClientProperty("FlatLaf.style", "arc: 24");
        tableContainer.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JScrollPane scrollPane = new JScrollPane(reportTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        
        // Top Container
        JPanel topContainer = new JPanel(new BorderLayout(10, 20));
        topContainer.setOpaque(false);
        topContainer.add(headerPanel, BorderLayout.NORTH);
        
        JPanel filterAndSummary = new JPanel(new BorderLayout(10, 25));
        filterAndSummary.setOpaque(false);
        filterAndSummary.add(controlPanel, BorderLayout.NORTH);
        filterAndSummary.add(summaryPanel, BorderLayout.CENTER);
        
        topContainer.add(filterAndSummary, BorderLayout.CENTER);
        
        add(topContainer, BorderLayout.NORTH);
        add(tableContainer, BorderLayout.CENTER);
    }
    
    private JPanel createSummaryCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.putClientProperty("FlatLaf.style", "arc: 24");
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 8, 0, 0, accentColor),
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleLabel.setForeground(new Color(71, 85, 105));
        
        valueLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 22));
        valueLabel.setForeground(new Color(15, 23, 42));
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    private void setupEventHandlers() {
        generateButton.addActionListener(e -> generateReport());
        reportTypeCombo.addActionListener(e -> generateReport());
        exportButton.addActionListener(e -> exportToCSV());
    }
    
    private void generateReport() {
        String selectedType = (String) reportTypeCombo.getSelectedItem();
        
        switch (selectedType) {
            case "Semua Order":
                generateOrderReport();
                break;
            case "Order Pending":
                generatePendingOrderReport();
                break;
            case "Order Selesai":
                generateCompletedOrderReport();
                break;
            case "Riwayat Pembayaran":
                generatePaymentReport();
                break;
        }
        
        loadSummary();
    }
    
    private void generateOrderReport() {
        tableModel.setRowCount(0);
        
        // Update table columns for orders
        String[] orderColumns = {"ID", "Tanggal", "Pelanggan", "Paket", "Berat", "Total", "Status"};
        tableModel.setColumnIdentifiers(orderColumns);
        
        List<LaundryOrder> orders = orderDAO.findAll();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        for (LaundryOrder order : orders) {
            Object[] row = {
                order.getId(),
                order.getOrderDate() != null ? order.getOrderDate().format(formatter) : "",
                order.getCustomerName(),
                order.getPackageName(),
                order.getWeightKg() + " kg",
                "Rp " + order.getTotalPrice(),
                order.getStatus()
            };
            tableModel.addRow(row);
        }
    }
    
    private void generatePendingOrderReport() {
        tableModel.setRowCount(0);
        
        String[] orderColumns = {"ID", "Tanggal", "Pelanggan", "Paket", "Berat", "Total", "Status"};
        tableModel.setColumnIdentifiers(orderColumns);
        
        List<LaundryOrder> orders = orderDAO.findAll();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        for (LaundryOrder order : orders) {
            if (!"completed".equals(order.getStatus())) {
                Object[] row = {
                    order.getId(),
                    order.getOrderDate() != null ? order.getOrderDate().format(formatter) : "",
                    order.getCustomerName(),
                    order.getPackageName(),
                    order.getWeightKg() + " kg",
                    "Rp " + order.getTotalPrice(),
                    order.getStatus()
                };
                tableModel.addRow(row);
            }
        }
    }
    
    private void generateCompletedOrderReport() {
        tableModel.setRowCount(0);
        
        String[] orderColumns = {"ID", "Tanggal", "Pelanggan", "Paket", "Berat", "Total", "Status"};
        tableModel.setColumnIdentifiers(orderColumns);
        
        List<LaundryOrder> orders = orderDAO.findAll();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        for (LaundryOrder order : orders) {
            if ("completed".equals(order.getStatus())) {
                Object[] row = {
                    order.getId(),
                    order.getOrderDate() != null ? order.getOrderDate().format(formatter) : "",
                    order.getCustomerName(),
                    order.getPackageName(),
                    order.getWeightKg() + " kg",
                    "Rp " + order.getTotalPrice(),
                    order.getStatus()
                };
                tableModel.addRow(row);
            }
        }
    }
    
    private void generatePaymentReport() {
        tableModel.setRowCount(0);
        
        // Update table columns for payments
        String[] paymentColumns = {"ID", "Order ID", "Pelanggan", "Jumlah", "Metode", "Tanggal", "Status"};
        tableModel.setColumnIdentifiers(paymentColumns);
        
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
    
    private void loadSummary() {
        List<LaundryOrder> allOrders = orderDAO.findAll();
        List<Payment> allPayments = paymentDAO.findAll();
        
        // Calculate totals
        int totalOrders = allOrders.size();
        int pendingOrders = 0;
        BigDecimal totalRevenue = BigDecimal.ZERO;
        
        for (LaundryOrder order : allOrders) {
            if (!"completed".equals(order.getStatus())) {
                pendingOrders++;
            }
        }
        
        for (Payment payment : allPayments) {
            totalRevenue = totalRevenue.add(payment.getAmount());
        }
        
        // Update labels
        totalOrdersLabel.setText("Total Orders: " + totalOrders);
        totalRevenueLabel.setText("Total Revenue: Rp " + totalRevenue);
        pendingOrdersLabel.setText("Pending Orders: " + pendingOrders);
    }
    
    private void exportToCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Simpan Laporan");
        
        String reportName = reportTypeCombo.getSelectedItem().toString().toLowerCase().replace(" ", "_");
        fileChooser.setSelectedFile(new java.io.File("laporan_" + reportName + ".csv"));
        
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            try (java.io.FileWriter fw = new java.io.FileWriter(fileToSave)) {
                // Write column headers
                for (int i = 0; i < tableModel.getColumnCount(); i++) {
                    fw.write(tableModel.getColumnName(i));
                    if (i < tableModel.getColumnCount() - 1) fw.write(",");
                }
                fw.write("\n");
                
                // Write row data
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    for (int j = 0; j < tableModel.getColumnCount(); j++) {
                        Object val = tableModel.getValueAt(i, j);
                        String str = (val == null) ? "" : val.toString().replace(",", ";"); // replace comma to avoid breaking CSV
                        fw.write(str);
                        if (j < tableModel.getColumnCount() - 1) fw.write(",");
                    }
                    fw.write("\n");
                }
                JOptionPane.showMessageDialog(this, "Laporan berhasil diexport ke:\n" + fileToSave.getAbsolutePath(), "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } catch (java.io.IOException e) {
                JOptionPane.showMessageDialog(this, "Gagal mengeksport file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void refreshData() {
        loadSummary();
        generateReport();
    }
}
