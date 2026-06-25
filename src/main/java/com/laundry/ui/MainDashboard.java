package com.laundry.ui;

import com.laundry.model.User;

import javax.swing.*;
import java.awt.*;

public class MainDashboard extends JFrame {
    private User currentUser;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    
    private DashboardHomePanel homePanel;
    private CustomerPanel customerPanel;
    private ServicePackagePanel packagePanel;
    private LaundryOrderPanel orderPanel;
    private PaymentPanel paymentPanel;
    private ReportPanel reportPanel;
    private UserPanel userPanel;
    
    public void showCard(String cardName) {
        cardLayout.show(contentPanel, cardName);
    }

    
    public MainDashboard(User user) {
        this.currentUser = user;
        initComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    private void initComponents() {
        setTitle("Dashboard Laundry - " + currentUser.getFullName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1000, 700));
        
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Header Panel (Top Bar)
        JPanel headerPanel = createHeaderPanel();
        
        // Sidebar Panel (Navigation)
        JPanel sidebarPanel = createSidebarPanel();
        
        // Content Panel with different forms
        setupContentPanels();
        
        // Main Container for content
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(new Color(26, 26, 39));
        mainContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainContainer.add(contentPanel, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);
        add(sidebarPanel, BorderLayout.WEST);
        add(mainContainer, BorderLayout.CENTER);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(0, 70));
        headerPanel.setBackground(new Color(30, 30, 46));
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(45, 45, 63)));
        
        JPanel leftHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 22));
        leftHeader.setOpaque(false);
        JLabel titleLabel = new JLabel("LAUNDRY MANAGEMENT");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(148, 163, 184));
        leftHeader.add(titleLabel);
        
        JPanel rightHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 25, 15));
        rightHeader.setOpaque(false);
        
        JLabel userLabel = new JLabel("<html><body style='text-align:right'><b>" + currentUser.getFullName() + "</b><br><font color='#94a3b8'>" + currentUser.getRole() + "</font></body></html>");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JButton logoutButton = new JButton("Log Out");
        logoutButton.putClientProperty("JButton.buttonType", "roundRect");
        logoutButton.setBackground(new Color(239, 68, 68));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin logout?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                new LoginFrame().setVisible(true);
                dispose();
            }
        });
        
        rightHeader.add(userLabel);
        rightHeader.add(Box.createHorizontalStrut(10));
        rightHeader.add(logoutButton);
        
        headerPanel.add(leftHeader, BorderLayout.WEST);
        headerPanel.add(rightHeader, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createSidebarPanel() {
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setPreferredSize(new Dimension(260, 0));
        sidebarPanel.setBackground(new Color(30, 30, 46));
        sidebarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(45, 45, 63)));
        
        // Top Padding to move menu up
        sidebarPanel.add(Box.createVerticalStrut(30));
        
        // Menu buttons
        String[][] menuItems = {
            {"Home", "home"},
            {"Statistik", "reports"},
            {"Pelanggan", "customers"},
            {"Paket", "packages"}, 
            {"Order Baru", "orders"},
            {"Pembayaran", "payments"},
            {"Pengaturan", "users"}
        };
        
        for (String[] item : menuItems) {
            JButton menuButton = createMenuButton(item[0], item[1]);
            sidebarPanel.add(menuButton);
            sidebarPanel.add(Box.createVerticalStrut(10));
        }
        
        sidebarPanel.add(Box.createVerticalGlue());
        
        return sidebarPanel;
    }
    
    private JButton createMenuButton(String text, String cardName) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(220, 50));
        button.setPreferredSize(new Dimension(220, 50));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 0));
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addActionListener(e -> {
            cardLayout.show(contentPanel, cardName);
            if ("home".equals(cardName) && homePanel != null) {
                homePanel.refreshData();
            } else if ("orders".equals(cardName) && orderPanel != null) {
                orderPanel.loadData();
            } else if ("payments".equals(cardName) && paymentPanel != null) {
                paymentPanel.loadData();
            } else if ("reports".equals(cardName) && reportPanel != null) {
                reportPanel.refreshData();
            }
        });
        
        return button;
    }
    
    private void setupContentPanels() {
        contentPanel.setOpaque(false);
        
        homePanel = new DashboardHomePanel();
        customerPanel = new CustomerPanel();
        packagePanel = new ServicePackagePanel();
        orderPanel = new LaundryOrderPanel();
        paymentPanel = new PaymentPanel();
        reportPanel = new ReportPanel();
        userPanel = new UserPanel();
        
        contentPanel.add(homePanel, "home");
        contentPanel.add(customerPanel, "customers");
        contentPanel.add(packagePanel, "packages");
        contentPanel.add(orderPanel, "orders");
        contentPanel.add(paymentPanel, "payments");
        contentPanel.add(reportPanel, "reports");
        contentPanel.add(userPanel, "users");
        
        cardLayout.show(contentPanel, "home");
        
        // Load initial dashboard statistics
        homePanel.refreshData();
    }
    
    private void setupEventHandlers() {
        // Event handlers are set up in individual methods
    }
}
