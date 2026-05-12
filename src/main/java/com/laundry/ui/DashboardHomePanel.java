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

    public DashboardHomePanel() {
        initComponents();
        setupLayout();
    }

    private void initComponents() {
        setOpaque(false);
    }

    private void setupLayout() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- WELCOME HEADER ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel welcomeLabel = new JLabel("Halo, Selamat Datang Kembali!");
        welcomeLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 28));
        welcomeLabel.setForeground(Color.WHITE);
        
        JLabel dateLabel = new JLabel("Berikut adalah ringkasan bisnis laundry Anda hari ini.");
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateLabel.setForeground(new Color(148, 163, 184));
        
        JPanel titleGroup = new JPanel();
        titleGroup.setLayout(new BoxLayout(titleGroup, BoxLayout.Y_AXIS));
        titleGroup.setOpaque(false);
        titleGroup.add(welcomeLabel);
        titleGroup.add(dateLabel);
        
        headerPanel.add(titleGroup, BorderLayout.WEST);

        // --- TOP STATS ---
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        statsPanel.setOpaque(false);
        statsPanel.setPreferredSize(new Dimension(0, 120));
        
        statsPanel.add(createMiniCard("Order Aktif", "12", new Color(99, 102, 241)));
        statsPanel.add(createMiniCard("Siap Ambil", "5", new Color(34, 197, 94)));
        statsPanel.add(createMiniCard("Pendapatan Hari Ini", "Rp 450.000", new Color(249, 115, 22)));
        statsPanel.add(createMiniCard("Pelanggan Baru", "3", new Color(168, 85, 247)));

        // --- CHART & RECENT AREA ---
        JPanel mainContent = new JPanel(new BorderLayout(20, 20));
        mainContent.setOpaque(false);

        // Chart Card
        JPanel chartCard = new JPanel(new BorderLayout());
        chartCard.setBackground(new Color(30, 30, 46));
        chartCard.putClientProperty("FlatLaf.style", "arc: 24");
        chartCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel chartTitle = new JLabel("Tren Pendapatan Mingguan");
        chartTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        chartTitle.setForeground(new Color(148, 163, 184));
        chartCard.add(chartTitle, BorderLayout.NORTH);
        
        chartCard.add(createRevenueChart(), BorderLayout.CENTER);

        mainContent.add(chartCard, BorderLayout.CENTER);

        // Right Sidebar (Quick Actions)
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
        actionPanel.setPreferredSize(new Dimension(250, 0));
        actionPanel.setBackground(new Color(30, 30, 46));
        actionPanel.putClientProperty("FlatLaf.style", "arc: 24");
        actionPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel actionTitle = new JLabel("Aksi Cepat");
        actionTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        actionTitle.setForeground(new Color(148, 163, 184));
        actionPanel.add(actionTitle);
        actionPanel.add(Box.createVerticalStrut(20));
        
        actionPanel.add(createActionButton("Buat Order Baru", new Color(99, 102, 241), e -> navigateTo("orders")));
        actionPanel.add(Box.createVerticalStrut(10));
        actionPanel.add(createActionButton("Tambah Pelanggan", new Color(30, 30, 46), e -> navigateTo("customers")));
        actionPanel.add(Box.createVerticalStrut(10));
        actionPanel.add(createActionButton("Cetak Laporan", new Color(30, 30, 46), e -> navigateTo("reports")));

        mainContent.add(actionPanel, BorderLayout.EAST);

        // --- ASSEMBLE ---
        JPanel topArea = new JPanel(new BorderLayout(0, 30));
        topArea.setOpaque(false);
        topArea.add(headerPanel, BorderLayout.NORTH);
        topArea.add(statsPanel, BorderLayout.SOUTH);

        add(topArea, BorderLayout.NORTH);
        add(mainContent, BorderLayout.CENTER);
    }

    private JPanel createMiniCard(String title, String value, Color accent) {
        JPanel card = new JPanel(new BorderLayout(0, 5));
        card.setBackground(new Color(30, 30, 46));
        card.putClientProperty("FlatLaf.style", "arc: 24");
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 4, 0, accent),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 11));
        t.setForeground(new Color(148, 163, 184));
        
        JLabel v = new JLabel(value);
        v.setFont(new Font("Segoe UI", Font.BOLD, 22));
        v.setForeground(Color.WHITE);
        
        card.add(t, BorderLayout.NORTH);
        card.add(v, BorderLayout.CENTER);
        return card;
    }

    private JButton createActionButton(String text, Color bg, java.awt.event.ActionListener listener) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setPreferredSize(new Dimension(0, 45));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.putClientProperty("JButton.buttonType", "roundRect");
        if(bg.equals(new Color(30, 30, 46))) {
            btn.setBorder(BorderFactory.createLineBorder(new Color(99, 102, 241), 1));
        }
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
        XYSeries series = new XYSeries("Revenue");
        series.add(1, 150000);
        series.add(2, 280000);
        series.add(3, 210000);
        series.add(4, 450000);
        series.add(5, 390000);
        series.add(6, 520000);
        series.add(7, 480000);

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        JFreeChart chart = ChartFactory.createXYLineChart(
                null, null, null, dataset,
                PlotOrientation.VERTICAL, false, true, false);

        chart.setBackgroundPaint(new Color(30, 30, 46));
        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(new Color(30, 30, 46));
        plot.setDomainGridlinePaint(new Color(45, 45, 63));
        plot.setRangeGridlinePaint(new Color(45, 45, 63));
        plot.setOutlineVisible(false);

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, new Color(99, 102, 241));
        renderer.setSeriesStroke(0, new BasicStroke(3.0f));
        plot.setRenderer(renderer);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setOpaque(false);
        chartPanel.setBackground(new Color(30, 30, 46));
        return chartPanel;
    }
}
