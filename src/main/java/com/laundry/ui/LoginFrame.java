package com.laundry.ui;

import com.laundry.dao.UserDAO;
import com.laundry.database.DatabaseManager;
import com.laundry.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class LoginFrame extends JFrame {

    // ── Palette ───────────────────────────────────────────────────────────────
    private static final Color C_LEFT_BG   = new Color(79,  70, 229);   // indigo
    private static final Color C_RIGHT_BG  = new Color(245, 247, 250);  // slate-50
    private static final Color C_WHITE     = Color.WHITE;
    private static final Color C_BORDER    = new Color(226, 232, 240);
    private static final Color C_TEXT_DARK = new Color(15,  23,  42);
    private static final Color C_TEXT_MUTE = new Color(100, 116, 139);
    private static final Color C_INPUT_BG  = new Color(248, 250, 252);
    private static final Color C_INPUT_BD  = new Color(203, 213, 225);
    private static final Color C_INDIGO    = new Color(99,  102, 241);
    private static final Color C_INDIGO_DK = new Color(79,  70, 229);
    private static final Color C_PURPLE    = new Color(196, 181, 253);
    private static final Color C_GREEN     = new Color(22,  163,  74);
    private static final Color C_RED       = new Color(220,  38,  38);

    // ── Fields ────────────────────────────────────────────────────────────────
    private JTextField     usernameField;
    private JPasswordField passwordField;
    private JLabel         statusLabel;
    private final UserDAO  userDAO;

    // ─────────────────────────────────────────────────────────────────────────
    public LoginFrame() {
        userDAO = new UserDAO();
        buildUI();
        checkDatabaseConnection();
    }

    // ── UI assembly ───────────────────────────────────────────────────────────
    private void buildUI() {
        setTitle("Login - Sistem Laundry");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(920, 580);
        setMinimumSize(new Dimension(800, 500));
        setLocationRelativeTo(null);
        setResizable(false);

        // Root: simple BorderLayout — left fixed, right takes rest
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(C_RIGHT_BG);
        setContentPane(root);

        // LEFT panel — fixed 400 px wide
        JPanel left = buildLeftPanel();
        root.add(left, BorderLayout.WEST);

        // RIGHT panel — centered login card
        JPanel right = buildRightPanel();
        root.add(right, BorderLayout.CENTER);
    }

    // ── Left branding panel ───────────────────────────────────────────────────
    private JPanel buildLeftPanel() {
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(C_LEFT_BG);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // subtle decorative circles
                g2.setColor(new Color(255, 255, 255, 18));
                g2.fillOval(-40, -40, 280, 280);
                g2.setColor(new Color(255, 255, 255, 10));
                g2.fillOval(getWidth() - 180, getHeight() - 180, 300, 300);
                g2.dispose();
            }
        };
        p.setLayout(new GridBagLayout());
        p.setOpaque(false);
        p.setBackground(C_LEFT_BG);
        p.setPreferredSize(new Dimension(400, 100)); // width respected by BorderLayout.WEST

        // Content box (BoxLayout Y) — this gets centered in the indigo panel
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setOpaque(false);

        // Brand name
        JLabel brand = new JLabel("Laundryyy");
        brand.setFont(new Font("Segoe UI", Font.BOLD, 38));
        brand.setForeground(C_WHITE);
        brand.setAlignmentX(Component.LEFT_ALIGNMENT);
        box.add(brand);

        box.add(Box.createVerticalStrut(8));

        // Tagline
        JLabel tag = new JLabel("<html>Sistem Manajemen Laundry<br>Modern &amp; Profesional</html>");
        tag.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tag.setForeground(C_PURPLE);
        tag.setAlignmentX(Component.LEFT_ALIGNMENT);
        box.add(tag);

        box.add(Box.createVerticalStrut(28));

        // Divider
        JPanel divider = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(new Color(255, 255, 255, 50));
                g.fillRect(0, 0, getWidth(), 1);
            }
            @Override public Dimension getPreferredSize()  { return new Dimension(260, 1); }
            @Override public Dimension getMaximumSize()    { return new Dimension(260, 1); }
        };
        divider.setOpaque(false);
        divider.setAlignmentX(Component.LEFT_ALIGNMENT);
        box.add(divider);

        box.add(Box.createVerticalStrut(22));

        // Feature bullets
        String[] feats = {
            "Kelola order & pembayaran",
            "Laporan & statistik real-time",
            "Manajemen pelanggan & paket"
        };
        for (String feat : feats) {
            box.add(buildBullet(feat));
            box.add(Box.createVerticalStrut(12));
        }

        // Center the box inside p using GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 44, 0, 24);
        p.add(box, gbc);

        return p;
    }

    private JPanel buildBullet(String text) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel dot = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(C_PURPLE);
                g2.fillOval(0, 5, 7, 7);
                g2.dispose();
            }
            @Override public Dimension getPreferredSize() { return new Dimension(10, 18); }
            @Override public boolean isOpaque()           { return false; }
        };

        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(new Color(221, 214, 254));

        row.add(dot);
        row.add(lbl);
        return row;
    }

    // ── Right login card ──────────────────────────────────────────────────────
    private JPanel buildRightPanel() {
        // Card IS the right panel — fills the space, draws rounded rect via paint
        JPanel card = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
            @Override public boolean isOpaque() { return true; }
        };
        card.setBackground(C_RIGHT_BG);

        // Inner white form panel — fixed width, centered vertically
        JPanel form = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(C_WHITE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                g2.setColor(C_BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth()-1f, getHeight()-1f, 20, 20));
                g2.dispose();
            }
            @Override public boolean isOpaque() { return false; }
        };
        form.setBackground(C_WHITE);

        GridBagConstraints r = new GridBagConstraints();
        r.gridx = 0;
        r.weightx = 1.0;
        r.fill = GridBagConstraints.HORIZONTAL;
        r.anchor = GridBagConstraints.WEST;
        int L = 40, R = 40; // left/right padding inside form

        // Title
        r.gridy = 0; r.insets = new Insets(44, L, 4, R);
        JLabel title = new JLabel("Selamat Datang");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(C_TEXT_DARK);
        form.add(title, r);

        // Subtitle
        r.gridy = 1; r.insets = new Insets(0, L, 24, R);
        JLabel sub = new JLabel("Masuk ke akun Laundryyy Anda");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sub.setForeground(C_TEXT_MUTE);
        form.add(sub, r);

        // USERNAME label
        r.gridy = 2; r.insets = new Insets(0, L, 5, R);
        JLabel lblUser = new JLabel("USERNAME");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblUser.setForeground(new Color(71, 85, 105));
        form.add(lblUser, r);

        // Username field
        r.gridy = 3; r.insets = new Insets(0, L, 14, R);
        usernameField = makeTextField("Masukkan username");
        form.add(usernameField, r);

        // PASSWORD label
        r.gridy = 4; r.insets = new Insets(0, L, 5, R);
        JLabel lblPass = new JLabel("PASSWORD");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblPass.setForeground(new Color(71, 85, 105));
        form.add(lblPass, r);

        // Password field
        r.gridy = 5; r.insets = new Insets(0, L, 24, R);
        passwordField = makePasswordField("Masukkan password");
        form.add(passwordField, r);

        // Login button
        r.gridy = 6; r.insets = new Insets(0, L, 10, R);
        JButton loginBtn = makeLoginButton();
        form.add(loginBtn, r);

        // Status label
        r.gridy = 7; r.insets = new Insets(0, L, 36, R);
        statusLabel = new JLabel("", JLabel.LEFT);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        form.add(statusLabel, r);

        // Place form centered in card
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(40, 30, 40, 40);
        form.setPreferredSize(new Dimension(420, 450));
        card.add(form, gbc);

        loginBtn.addActionListener(e -> performLogin());
        getRootPane().setDefaultButton(loginBtn);

        return card;
    }

    // Wrap a field so it has left/right insets and stretches full width
    private JPanel wrapField(JComponent field, int px) {
        JPanel w = new JPanel(new BorderLayout());
        w.setOpaque(false);
        w.setBorder(BorderFactory.createEmptyBorder(0, px, 0, px));
        w.add(field, BorderLayout.CENTER);
        w.setAlignmentX(Component.LEFT_ALIGNMENT);
        w.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        return w;
    }

    // Wrap button full width
    private JPanel wrapFull(JComponent comp, int px) {
        JPanel w = new JPanel(new BorderLayout());
        w.setOpaque(false);
        w.setBorder(BorderFactory.createEmptyBorder(0, px, 0, px));
        w.add(comp, BorderLayout.CENTER);
        w.setAlignmentX(Component.LEFT_ALIGNMENT);
        w.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        return w;
    }

    private JLabel makeFieldLabel(String text, int px) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(new Color(71, 85, 105));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setBorder(BorderFactory.createEmptyBorder(0, px, 0, px));
        lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        return lbl;
    }

    private JTextField makeTextField(String placeholder) {
        JTextField f = new JTextField() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(C_INPUT_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.setColor(hasFocus() ? C_INDIGO : C_INPUT_BD);
                g2.setStroke(new BasicStroke(hasFocus() ? 1.5f : 1f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth()-1, getHeight()-1, 10, 10));
                g2.dispose();
                super.paintComponent(g);
            }
            @Override public boolean isOpaque() { return false; }
        };
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setForeground(C_TEXT_DARK);
        f.setCaretColor(C_INDIGO);
        f.setBackground(C_INPUT_BG);
        f.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 14));
        f.putClientProperty("JTextField.placeholderText", placeholder);
        f.putClientProperty("FlatLaf.style", "border: null");
        f.setPreferredSize(new Dimension(0, 46));
        f.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { f.repaint(); }
            public void focusLost(FocusEvent e)   { f.repaint(); }
        });
        return f;
    }

    private JPasswordField makePasswordField(String placeholder) {
        JPasswordField f = new JPasswordField() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(C_INPUT_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.setColor(hasFocus() ? C_INDIGO : C_INPUT_BD);
                g2.setStroke(new BasicStroke(hasFocus() ? 1.5f : 1f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth()-1, getHeight()-1, 10, 10));
                g2.dispose();
                super.paintComponent(g);
            }
            @Override public boolean isOpaque() { return false; }
        };
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setForeground(C_TEXT_DARK);
        f.setCaretColor(C_INDIGO);
        f.setBackground(C_INPUT_BG);
        f.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 14));
        f.putClientProperty("JTextField.placeholderText", placeholder);
        f.putClientProperty("FlatLaf.style", "border: null");
        f.setPreferredSize(new Dimension(0, 46));
        f.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { f.repaint(); }
            public void focusLost(FocusEvent e)   { f.repaint(); }
        });
        return f;
    }

    private JButton makeLoginButton() {
        JButton btn = new JButton("MASUK SEKARANG") {
            private boolean hovered;
            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
                    public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hovered ? C_INDIGO_DK : C_INDIGO);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.dispose();
                super.paintComponent(g);
            }
            @Override public boolean isOpaque() { return false; }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, 50));
        return btn;
    }

    // ── Logic ─────────────────────────────────────────────────────────────────
    private void checkDatabaseConnection() {
        if (DatabaseManager.testDatabaseConnection()) {
            statusLabel.setText("Database terhubung");
            statusLabel.setForeground(C_GREEN);
        } else {
            statusLabel.setText("Database tidak terhubung - Periksa XAMPP MySQL");
            statusLabel.setForeground(C_RED);
        }
    }

    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Username dan password harus diisi!", "Perhatian", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!DatabaseManager.testDatabaseConnection()) {
            JOptionPane.showMessageDialog(this,
                "Tidak dapat terhubung ke database!\n\nPastikan XAMPP MySQL sudah berjalan:\n"
                + "1. Buka XAMPP Control Panel\n2. Start Apache & MySQL\n3. Pastikan port 3306 tidak diblokir",
                "Database Error", JOptionPane.ERROR_MESSAGE);
            checkDatabaseConnection();
            return;
        }

        User user = userDAO.authenticate(username, password);
        if (user != null) {
            JOptionPane.showMessageDialog(this,
                "Login berhasil! Selamat datang " + user.getFullName(), "Sukses", JOptionPane.INFORMATION_MESSAGE);
            SwingUtilities.invokeLater(() -> {
                new MainDashboard(user).setVisible(true);
                dispose();
            });
        } else {
            JOptionPane.showMessageDialog(this,
                "Username atau password salah!", "Login Gagal", JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
            passwordField.requestFocus();
        }
    }
}
