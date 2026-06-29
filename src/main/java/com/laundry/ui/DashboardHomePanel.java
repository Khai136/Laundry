package com.laundry.ui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;

public class DashboardHomePanel extends JPanel {
    private JLabel activeOrdersValueLabel;
    private JLabel readyOrdersValueLabel;
    private JLabel todayRevenueValueLabel;
    private JLabel newCustomersValueLabel;
    private XYSeries revenueSeries;

    public DashboardHomePanel() {
        initComponents();
        setupLayout();
    }

    private void initComponents() {
        setOpaque(false);
    }

    private void setupLayout() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- WELCOME HEADER ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel welcomeLabel = new JLabel("Halo, Selamat Datang Kembali!");
        welcomeLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 28));
        welcomeLabel.setForeground(new Color(15, 23, 42));

        JLabel dateLabel = new JLabel("Berikut adalah ringkasan bisnis laundry Anda hari ini.");
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateLabel.setForeground(new Color(100, 116, 139));
        
        JPanel titleGroup = new JPanel();
        titleGroup.setLayout(new BoxLayout(titleGroup, BoxLayout.Y_AXIS));
        titleGroup.setOpaque(false);
        titleGroup.add(welcomeLabel);
        titleGroup.add(dateLabel);
        
        headerPanel.add(titleGroup, BorderLayout.WEST);

        // --- TOP STATS ---
        activeOrdersValueLabel = new JLabel("0");
        readyOrdersValueLabel = new JLabel("0");
        todayRevenueValueLabel = new JLabel("Rp 0");
        newCustomersValueLabel = new JLabel("0");

        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        statsPanel.setOpaque(false);
        statsPanel.setPreferredSize(new Dimension(0, 120));
        
        statsPanel.add(createMiniCard("Order Aktif", activeOrdersValueLabel, new Color(99, 102, 241)));
        statsPanel.add(createMiniCard("Siap Ambil", readyOrdersValueLabel, new Color(34, 197, 94)));
        statsPanel.add(createMiniCard("Pendapatan Hari Ini", todayRevenueValueLabel, new Color(249, 115, 22)));
        statsPanel.add(createMiniCard("Pelanggan Baru", newCustomersValueLabel, new Color(168, 85, 247)));

        // --- CHART & RECENT AREA ---
        JPanel mainContent = new JPanel(new BorderLayout(20, 20));
        mainContent.setOpaque(false);

        // Chart Card
        JPanel chartCard = new JPanel(new BorderLayout());
        chartCard.setBackground(Color.WHITE);
        chartCard.putClientProperty("FlatLaf.style", "arc: 24");
        chartCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel chartTitle = new JLabel("Tren Pendapatan Mingguan");
        chartTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        chartTitle.setForeground(new Color(71, 85, 105));
        chartCard.add(chartTitle, BorderLayout.NORTH);
        
        chartCard.add(createRevenueChart(), BorderLayout.CENTER);

        mainContent.add(chartCard, BorderLayout.CENTER);

        // Right Sidebar (Quick Actions)
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
        actionPanel.setPreferredSize(new Dimension(250, 0));
        actionPanel.setBackground(Color.WHITE);
        actionPanel.putClientProperty("FlatLaf.style", "arc: 24");
        actionPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel actionTitle = new JLabel("Aksi Cepat");
        actionTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        actionTitle.setForeground(new Color(71, 85, 105));
        actionPanel.add(actionTitle);
        actionPanel.add(Box.createVerticalStrut(20));
        
        actionPanel.add(createActionButton("Buat Order Baru", new Color(99, 102, 241), e -> navigateTo("orders")));
        actionPanel.add(Box.createVerticalStrut(10));
        actionPanel.add(createActionButton("Tambah Pelanggan", Color.WHITE, e -> navigateTo("customers")));
        actionPanel.add(Box.createVerticalStrut(10));
        actionPanel.add(createActionButton("Cetak Laporan", Color.WHITE, e -> navigateTo("reports")));

        mainContent.add(actionPanel, BorderLayout.EAST);

        // --- ASSEMBLE ---
        JPanel topArea = new JPanel(new BorderLayout(0, 30));
        topArea.setOpaque(false);
        topArea.add(headerPanel, BorderLayout.NORTH);
        topArea.add(statsPanel, BorderLayout.SOUTH);

        add(topArea, BorderLayout.NORTH);
        add(mainContent, BorderLayout.CENTER);
    }

    private JPanel createMiniCard(String title, JLabel valueLabel, Color accent) {
        JPanel card = new JPanel(new BorderLayout(0, 5));
        card.setBackground(Color.WHITE);
        card.putClientProperty("FlatLaf.style", "arc: 24");
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 4, 0, accent),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 11));
        t.setForeground(new Color(71, 85, 105));
        
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valueLabel.setForeground(new Color(15, 23, 42));
        
        card.add(t, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    private JButton createActionButton(String text, Color bg, java.awt.event.ActionListener listener) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setPreferredSize(new Dimension(0, 45));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        if (bg.equals(Color.WHITE)) {
            btn.setForeground(new Color(99, 102, 241));
            btn.setBorder(BorderFactory.createLineBorder(new Color(99, 102, 241), 1));
        } else {
            btn.setForeground(Color.WHITE);
        }
        btn.putClientProperty("JButton.buttonType", "roundRect");
        if (listener != null) {
            btn.addActionListener(listener);
        }
        return btn;
    }

    private void navigateTo(String cardName) {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof MainDashboard) {
            ((MainDashboard) window).showCard(cardName);
        }
    }

    private ChartPanel createRevenueChart() {
        revenueSeries = new XYSeries("Revenue");
        // default dummy points
        revenueSeries.add(1, 0);

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(revenueSeries);

        JFreeChart chart = ChartFactory.createXYLineChart(
                null, null, null, dataset,
                PlotOrientation.VERTICAL, false, true, false);

        chart.setBackgroundPaint(Color.WHITE);
        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(new Color(226, 232, 240));
        plot.setRangeGridlinePaint(new Color(226, 232, 240));
        plot.setOutlineVisible(false);
        
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, new Color(99, 102, 241));
        renderer.setSeriesStroke(0, new BasicStroke(3.0f));
        plot.setRenderer(renderer);
        
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setOpaque(false);
        chartPanel.setBackground(Color.WHITE);
        return chartPanel;
    }
    
    public void refreshData() {
        com.laundry.dao.LaundryOrderDAO orderDAO = new com.laundry.dao.LaundryOrderDAO();
        com.laundry.dao.PaymentDAO paymentDAO = new com.laundry.dao.PaymentDAO();
        com.laundry.dao.CustomerDAO customerDAO = new com.laundry.dao.CustomerDAO();
        
        int activeOrders = orderDAO.getActiveOrdersCount();
        int readyOrders = orderDAO.getReadyOrdersCount();
        java.math.BigDecimal todayRevenue = paymentDAO.getTodayRevenue();
        int newCustomers = customerDAO.getNewCustomersTodayCount();
        
        activeOrdersValueLabel.setText(String.valueOf(activeOrders));
        readyOrdersValueLabel.setText(String.valueOf(readyOrders));
        
        java.text.DecimalFormat df = new java.text.DecimalFormat("#,##0");
        todayRevenueValueLabel.setText("Rp " + df.format(todayRevenue));
        newCustomersValueLabel.setText(String.valueOf(newCustomers));
        
        // Update weekly revenue chart
        revenueSeries.clear();
        java.util.List<Object[]> weeklyData = paymentDAO.getWeeklyRevenue();
        
        if (weeklyData.isEmpty()) {
            revenueSeries.add(1, 0);
            revenueSeries.add(2, 0);
        } else {
            int index = 1;
            for (Object[] row : weeklyData) {
                java.math.BigDecimal amount = (java.math.BigDecimal) row[1];
                revenueSeries.add(index++, amount.doubleValue());
            }
        }
    }
}
