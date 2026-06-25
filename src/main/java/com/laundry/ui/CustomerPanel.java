package com.laundry.ui;

import com.laundry.dao.CustomerDAO;
import com.laundry.model.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import javax.swing.ListSelectionModel;

public class CustomerPanel extends JPanel {
    private CustomerDAO customerDAO;
    private JTable customerTable;
    private DefaultTableModel tableModel;
    private JTextField nameField, phoneField, emailField;
    private JTextArea addressArea;
    private JButton saveButton, updateButton, deleteButton, clearButton;
    private Customer selectedCustomer;
    private JTextField searchField;
    private JLabel formTitle;
    
    public CustomerPanel() {
        customerDAO = new CustomerDAO();
        initComponents();
        setupLayout();
        setupEventHandlers();
        loadCustomers();
        updateFormMode(false);
    }
    
    private void initComponents() {
        // Form fields
        nameField = new JTextField(20);
        phoneField = new JTextField(20);
        emailField = new JTextField(20);
        addressArea = new JTextArea(3, 20);
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
        
        // Search field
        searchField = new JTextField(15);
        searchField.putClientProperty("JTextField.placeholderText", "Cari nama atau telepon...");
        
        // Buttons
        saveButton = new JButton("Simpan");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Hapus");
        clearButton = new JButton("Clear");
        
        // Table
        String[] columns = {"ID", "Nama", "Telepon", "Email", "Alamat"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        customerTable = new JTable(tableModel);
        customerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(20, 20));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel titleLabel = new JLabel("Data Pelanggan");
        titleLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // Form Panel (Left)
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setPreferredSize(new Dimension(350, 0));
        formContainer.setBackground(new Color(30, 30, 46));
        formContainer.putClientProperty("FlatLaf.style", "arc: 24");
        formContainer.setBorder(BorderFactory.createEmptyBorder(30, 25, 30, 25));
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Form Header
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formTitle = new JLabel("Tambah Pelanggan Baru");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        formTitle.setForeground(new Color(99, 102, 241));
        formPanel.add(formTitle, gbc);
        
        gbc.gridwidth = 1;
        // Name
        gbc.gridy++; gbc.insets = new Insets(20, 5, 5, 5);
        formPanel.add(new JLabel("Nama Lengkap"), gbc);
        gbc.gridy++; gbc.insets = new Insets(0, 5, 15, 5);
        nameField.setPreferredSize(new Dimension(0, 40));
        formPanel.add(nameField, gbc);
        
        // Phone
        gbc.gridy++; gbc.insets = new Insets(5, 5, 5, 5);
        formPanel.add(new JLabel("No. Telepon"), gbc);
        gbc.gridy++; gbc.insets = new Insets(0, 5, 15, 5);
        phoneField.setPreferredSize(new Dimension(0, 40));
        formPanel.add(phoneField, gbc);
        
        // Email (Keeping for logic consistency)
        gbc.gridy++;
        formPanel.add(new JLabel("Email"), gbc);
        gbc.gridy++;
        emailField.putClientProperty("JTextField.placeholderText", "contoh@mail.com");
        formPanel.add(emailField, gbc);
        
        // Address
        gbc.gridy++; gbc.insets = new Insets(5, 5, 5, 5);
        formPanel.add(new JLabel("Alamat"), gbc);
        gbc.gridy++; gbc.insets = new Insets(0, 5, 20, 5);
        gbc.weighty = 1.0; gbc.fill = GridBagConstraints.BOTH;
        JScrollPane addrScroll = new JScrollPane(addressArea);
        addrScroll.setPreferredSize(new Dimension(0, 100));
        formPanel.add(addrScroll, gbc);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 12, 12));
        buttonPanel.setOpaque(false);
        saveButton.setBackground(new Color(99, 102, 241));
        saveButton.setForeground(Color.WHITE);
        
        saveButton.putClientProperty("JButton.buttonType", "roundRect");
        updateButton.putClientProperty("JButton.buttonType", "roundRect");
        deleteButton.putClientProperty("JButton.buttonType", "roundRect");
        clearButton.putClientProperty("JButton.buttonType", "roundRect");
        
        buttonPanel.add(saveButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        
        gbc.gridy++; gbc.weighty = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(20, 5, 5, 5);
        formPanel.add(buttonPanel, gbc);
        
        formContainer.add(formPanel, BorderLayout.NORTH);
        
        // Table Panel (Right)
        JPanel tableContainer = new JPanel(new BorderLayout(0, 10));
        tableContainer.setBackground(new Color(30, 30, 46));
        tableContainer.putClientProperty("FlatLaf.style", "arc: 24");
        tableContainer.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);
        searchPanel.add(new JLabel("Cari: "));
        searchPanel.add(searchField);
        tableContainer.add(searchPanel, BorderLayout.NORTH);
        
        JScrollPane scrollPane = new JScrollPane(customerTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);
        add(formContainer, BorderLayout.WEST);
        add(tableContainer, BorderLayout.CENTER);
    }
    
    private void setupEventHandlers() {
        saveButton.addActionListener(e -> saveCustomer());
        updateButton.addActionListener(e -> updateCustomer());
        deleteButton.addActionListener(e -> deleteCustomer());
        clearButton.addActionListener(e -> clearForm());
        
        // Row filter sorter for live search
        javax.swing.table.TableRowSorter<DefaultTableModel> sorter = new javax.swing.table.TableRowSorter<>(tableModel);
        customerTable.setRowSorter(sorter);
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            
            private void filter() {
                String text = searchField.getText();
                if (text.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(text)));
                }
            }
        });
        
        // Right click selection & Popup Menu
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem editItem = new JMenuItem("Edit Pelanggan");
        JMenuItem deleteItem = new JMenuItem("Hapus Pelanggan");
        JMenuItem copyPhoneItem = new JMenuItem("Salin No. Telepon");
        
        popupMenu.add(editItem);
        popupMenu.add(deleteItem);
        popupMenu.addSeparator();
        popupMenu.add(copyPhoneItem);
        
        customerTable.setComponentPopupMenu(popupMenu);
        
        customerTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                if (javax.swing.SwingUtilities.isRightMouseButton(e)) {
                    int r = customerTable.rowAtPoint(e.getPoint());
                    if (r >= 0 && r < customerTable.getRowCount()) {
                        customerTable.setRowSelectionInterval(r, r);
                    } else {
                        customerTable.clearSelection();
                    }
                }
            }
        });
        
        editItem.addActionListener(e -> {
            selectCustomer();
            nameField.requestFocus();
        });
        deleteItem.addActionListener(e -> deleteCustomer());
        copyPhoneItem.addActionListener(e -> {
            if (selectedCustomer != null) {
                java.awt.datatransfer.StringSelection stringSelection = new java.awt.datatransfer.StringSelection(selectedCustomer.getPhone());
                java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
                JOptionPane.showMessageDialog(CustomerPanel.this, "Nomor telepon disalin ke clipboard!");
            }
        });
        
        // Keyboard shortcuts
        customerTable.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
            .put(KeyStroke.getKeyStroke("DELETE"), "deleteCustomer");
        customerTable.getActionMap().put("deleteCustomer", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                deleteCustomer();
            }
        });
        
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke("ESCAPE"), "clearForm");
        getActionMap().put("clearForm", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                clearForm();
            }
        });
        
        // Enter key in text fields
        java.awt.event.ActionListener fieldSubmitAction = e -> {
            if (selectedCustomer == null) {
                saveCustomer();
            } else {
                updateCustomer();
            }
        };
        nameField.addActionListener(fieldSubmitAction);
        phoneField.addActionListener(fieldSubmitAction);
        emailField.addActionListener(fieldSubmitAction);
        
        customerTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectCustomer();
            }
        });
    }
    
    private void saveCustomer() {
        if (validateForm()) {
            Customer customer = new Customer();
            customer.setName(nameField.getText().trim());
            customer.setPhone(phoneField.getText().trim());
            customer.setEmail(emailField.getText().trim());
            customer.setAddress(addressArea.getText().trim());
            
            if (customerDAO.save(customer)) {
                JOptionPane.showMessageDialog(this, "Data pelanggan berhasil disimpan!");
                clearForm();
                loadCustomers();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan data pelanggan!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void updateCustomer() {
        if (selectedCustomer != null && validateForm()) {
            selectedCustomer.setName(nameField.getText().trim());
            selectedCustomer.setPhone(phoneField.getText().trim());
            selectedCustomer.setEmail(emailField.getText().trim());
            selectedCustomer.setAddress(addressArea.getText().trim());
            
            if (customerDAO.update(selectedCustomer)) {
                JOptionPane.showMessageDialog(this, "Data pelanggan berhasil diupdate!");
                clearForm();
                loadCustomers();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal mengupdate data pelanggan!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void deleteCustomer() {
        if (selectedCustomer != null) {
            int result = JOptionPane.showConfirmDialog(this, 
                "Apakah Anda yakin ingin menghapus pelanggan " + selectedCustomer.getName() + "?", 
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
            
            if (result == JOptionPane.YES_OPTION) {
                if (customerDAO.delete(selectedCustomer.getId())) {
                    JOptionPane.showMessageDialog(this, "Data pelanggan berhasil dihapus!");
                    clearForm();
                    loadCustomers();
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal menghapus data pelanggan!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private void clearForm() {
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        addressArea.setText("");
        selectedCustomer = null;
        customerTable.clearSelection();
        updateFormMode(false);
    }
    
    private void selectCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = customerTable.convertRowIndexToModel(selectedRow);
            int customerId = (Integer) tableModel.getValueAt(modelRow, 0);
            selectedCustomer = customerDAO.findById(customerId);
            
            if (selectedCustomer != null) {
                nameField.setText(selectedCustomer.getName());
                phoneField.setText(selectedCustomer.getPhone());
                emailField.setText(selectedCustomer.getEmail());
                addressArea.setText(selectedCustomer.getAddress());
                updateFormMode(true);
            }
        }
    }
    
    private void updateFormMode(boolean isEditMode) {
        if (isEditMode) {
            if (selectedCustomer != null) {
                formTitle.setText("Edit Pelanggan (ID: " + selectedCustomer.getId() + ")");
            }
            formTitle.setForeground(new Color(249, 115, 22)); // Orange
            saveButton.setEnabled(false);
            updateButton.setEnabled(true);
            deleteButton.setEnabled(true);
        } else {
            formTitle.setText("Tambah Pelanggan Baru");
            formTitle.setForeground(new Color(99, 102, 241)); // Indigo
            saveButton.setEnabled(true);
            updateButton.setEnabled(false);
            deleteButton.setEnabled(false);
        }
    }
    
    private boolean validateForm() {
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama harus diisi!", "Validasi", JOptionPane.WARNING_MESSAGE);
            nameField.requestFocus();
            return false;
        }
        if (phoneField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Telepon harus diisi!", "Validasi", JOptionPane.WARNING_MESSAGE);
            phoneField.requestFocus();
            return false;
        }
        return true;
    }
    
    private void loadCustomers() {
        tableModel.setRowCount(0);
        List<Customer> customers = customerDAO.findAll();
        
        for (Customer customer : customers) {
            Object[] row = {
                customer.getId(),
                customer.getName(),
                customer.getPhone(),
                customer.getEmail(),
                customer.getAddress()
            };
            tableModel.addRow(row);
        }
    }
}
