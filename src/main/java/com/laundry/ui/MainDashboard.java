package com.laundry.ui;

import com.laundry.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class MainDashboard extends JFrame {

    // ── Colour Palette (Light Theme) ──────────────────────────────────────────
    private static final Color BG_PAGE     = new Color(245, 247, 250);
    private static final Color SIDEBAR_BG  = new Color(255, 255, 255);
    private static final Color SIDEBAR_BDR = new Color(226, 232, 240);
    private static final Color HEADER_BG   = new Color(255, 255, 255);
    private static final Color HEADER_BDR  = new Color(226, 232, 240);
    private static final Color CONTENT_BG  = new Color(245, 247, 250);
    private static final Color ACCENT      = new Color(15,  23,  42);
    private static final Color ACCENT_DIM  = new Color(100, 116, 139);
    private static final Color MENU_HOVER  = new Color(241, 245, 249);
    private static final Color MENU_ACTIVE = new Color(99,  102, 241);
    private static final Color MENU_ACT_FG = new Color(255, 255, 255);
    private static final Color RED_LOGOUT  = new Color(239,  68,  68);
    private static final Color SECTION_LBL = new Color(148, 163, 184);

    // ─────────────────────────────────────────────────────────────────────────
    private User        currentUser;
    private JPanel      contentPanel;
    private CardLayout  cardLayout;
    private JButton     activeMenuBtn;

    private DashboardHomePanel  homePanel;
    private CustomerPanel       customerPanel;
    private ServicePackagePanel packagePanel;
    private LaundryOrderPanel   orderPanel;
    private PaymentPanel        paymentPanel;
    private ReportPanel         reportPanel;
    private UserPanel           userPanel;

    public void showCard(String cardName) {
        cardLayout.show(contentPanel, cardName);
    }

    // ─────────────────────────────────────────────────────────────────────────
    public MainDashboard(User user) {
        this.currentUser = user;
        initComponents();
        setupLayout();
    }

    private void initComponents() {
        setTitle("Dashboard Laundry - " + currentUser.getFullName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1100, 700));

        cardLayout   = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(CONTENT_BG);
        contentPanel.setOpaque(true);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_PAGE);

        add(buildHeader(),  BorderLayout.NORTH);
        add(buildSidebar(), BorderLayout.WEST);

        JPanel mainArea = new JPanel(new BorderLayout());
        mainArea.setBackground(BG_PAGE);
        mainArea.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setupContentPanels();
        mainArea.add(contentPanel, BorderLayout.CENTER);
        add(mainArea, BorderLayout.CENTER);
    }

    // ── Header ────────────────────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(HEADER_BG);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setPreferredSize(new Dimension(0, 64));
        header.setOpaque(false);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, HEADER_BDR));

        // Left: color accent dot + app name
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        left.setOpaque(false);
        left.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        left.setPreferredSize(new Dimension(200, 64));
        left.setAlignmentY(Component.CENTER_ALIGNMENT);

        // Accent dot logo
        JPanel logoDot = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Gradient dot
                java.awt.GradientPaint gp = new java.awt.GradientPaint(
                    0, 0, new Color(99, 102, 241),
                    10, 10, new Color(168, 85, 247));
                g2.setPaint(gp);
                g2.fillOval(0, 0, 10, 10);
                g2.dispose();
            }
            @Override public Dimension getPreferredSize() { return new Dimension(10, 10); }
            @Override public boolean isOpaque() { return false; }
        };

        JLabel appName = new JLabel("Laundryyy");
        appName.setFont(new Font("Segoe UI", Font.BOLD, 18));
        appName.setForeground(new Color(15, 23, 42));
        appName.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));

        left.add(logoDot);
        left.add(appName);
        header.add(left, BorderLayout.WEST);

        // Right: user name + role badge + logout
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 16));
        right.setOpaque(false);

        JLabel nameLbl = new JLabel(currentUser.getFullName());
        nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nameLbl.setForeground(new Color(15, 23, 42));

        // Role badge
        JLabel roleLbl = new JLabel(currentUser.getRole()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(224, 231, 255));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
            @Override public boolean isOpaque() { return false; }
        };
        roleLbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        roleLbl.setForeground(new Color(79, 70, 229));
        roleLbl.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));

        JButton logoutBtn = buildLogoutButton();

        right.add(nameLbl);
        right.add(roleLbl);
        right.add(logoutBtn);
        header.add(right, BorderLayout.EAST);

        return header;
    }

    private JButton buildLogoutButton() {
        JButton btn = new JButton("Keluar") {
            private boolean hov = false;
            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hov = true;  repaint(); }
                    public void mouseExited(MouseEvent e)  { hov = false; repaint(); }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hov ? RED_LOGOUT.darker() : RED_LOGOUT);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.dispose();
                super.paintComponent(g);
            }
            @Override public boolean isOpaque() { return false; }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(76, 32));
        btn.addActionListener(e -> {
            int r = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin keluar?",
                "Konfirmasi Logout", JOptionPane.YES_NO_OPTION);
            if (r == JOptionPane.YES_OPTION) {
                new LoginFrame().setVisible(true);
                dispose();
            }
        });
        return btn;
    }

    // ── Sidebar ───────────────────────────────────────────────────────────────
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(SIDEBAR_BG);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setOpaque(false);
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, SIDEBAR_BDR));

        sidebar.add(Box.createVerticalStrut(20));

        sidebar.add(sectionLabel("MENU UTAMA"));
        sidebar.add(Box.createVerticalStrut(6));

        addMenuBtn(sidebar, "Dashboard",       "home",      true);
        addMenuBtn(sidebar, "Order Baru",       "orders",    false);
        addMenuBtn(sidebar, "Pembayaran",       "payments",  false);
        addMenuBtn(sidebar, "Pelanggan",        "customers", false);

        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(sectionLabel("MANAJEMEN"));
        sidebar.add(Box.createVerticalStrut(6));

        addMenuBtn(sidebar, "Statistik",        "reports",   false);
        addMenuBtn(sidebar, "Paket Layanan",    "packages",  false);
        addMenuBtn(sidebar, "Pengaturan User",  "users",     false);

        sidebar.add(Box.createVerticalGlue());
        sidebar.add(Box.createVerticalStrut(12));

        return sidebar;
    }

    private JLabel sectionLabel(String text) {
        JLabel lbl = new JLabel("  " + text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lbl.setForeground(SECTION_LBL);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setMaximumSize(new Dimension(230, 20));
        return lbl;
    }

    private void addMenuBtn(JPanel sidebar, String text, String cardName, boolean isDefault) {
        JButton btn = new JButton(text) {
            private boolean hov = false;
            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hov = true;  repaint(); }
                    public void mouseExited(MouseEvent e)  { hov = false; repaint(); }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean active = Boolean.TRUE.equals(getClientProperty("active"));
                if (active) {
                    g2.setColor(MENU_ACTIVE);
                    g2.fill(new RoundRectangle2D.Float(8, 3, getWidth() - 16, getHeight() - 6, 10, 10));
                    setForeground(MENU_ACT_FG);
                } else if (hov) {
                    g2.setColor(MENU_HOVER);
                    g2.fill(new RoundRectangle2D.Float(8, 3, getWidth() - 16, getHeight() - 6, 10, 10));
                    setForeground(new Color(15, 23, 42));
                } else {
                    setForeground(ACCENT_DIM);
                }
                g2.dispose();
                super.paintComponent(g);
            }
            @Override public boolean isOpaque() { return false; }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(220, 42));
        btn.setPreferredSize(new Dimension(220, 42));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        if (isDefault) {
            btn.putClientProperty("active", true);
            activeMenuBtn = btn;
        }

        btn.addActionListener(e -> {
            if (activeMenuBtn != null) {
                activeMenuBtn.putClientProperty("active", false);
                activeMenuBtn.repaint();
            }
            btn.putClientProperty("active", true);
            btn.repaint();
            activeMenuBtn = btn;
            cardLayout.show(contentPanel, cardName);

            if ("home".equals(cardName)     && homePanel    != null) homePanel.refreshData();
            if ("orders".equals(cardName)   && orderPanel   != null) orderPanel.loadData();
            if ("payments".equals(cardName) && paymentPanel != null) paymentPanel.loadData();
            if ("reports".equals(cardName)  && reportPanel  != null) reportPanel.refreshData();
        });

        sidebar.add(btn);
        sidebar.add(Box.createVerticalStrut(2));
    }

    // ── Content panels ────────────────────────────────────────────────────────
    private void setupContentPanels() {
        homePanel     = new DashboardHomePanel();
        customerPanel = new CustomerPanel();
        packagePanel  = new ServicePackagePanel();
        orderPanel    = new LaundryOrderPanel();
        paymentPanel  = new PaymentPanel();
        reportPanel   = new ReportPanel();
        userPanel     = new UserPanel();

        contentPanel.add(homePanel,     "home");
        contentPanel.add(customerPanel, "customers");
        contentPanel.add(packagePanel,  "packages");
        contentPanel.add(orderPanel,    "orders");
        contentPanel.add(paymentPanel,  "payments");
        contentPanel.add(reportPanel,   "reports");
        contentPanel.add(userPanel,     "users");

        cardLayout.show(contentPanel, "home");
        homePanel.refreshData();
    }

    private void setupEventHandlers() {
        // handled in individual methods
    }
}
