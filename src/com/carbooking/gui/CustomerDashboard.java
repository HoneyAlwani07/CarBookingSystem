package com.carbooking.gui;

import com.carbooking.model.User;
import com.carbooking.db.DatabaseConnection;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

public class CustomerDashboard extends JFrame {

    private User currentUser;
    private JFrame previousScreen;

    public CustomerDashboard(User user, JFrame previous) {
        this.currentUser = user;
        this.previousScreen = previous;
        setTitle("Car Booking System - Customer Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 650);
        setLocationRelativeTo(null);
        setResizable(false);
        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UITheme.BG_DARK);

        // TOP BAR
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(UITheme.BG_CARD);
        topBar.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JLabel titleLabel = new JLabel("🚗  Car Booking System");
        titleLabel.setFont(UITheme.FONT_HEADING);
        titleLabel.setForeground(UITheme.ACCENT_BLUE);
        topBar.add(titleLabel, BorderLayout.WEST);

        JPanel userInfo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        userInfo.setBackground(UITheme.BG_CARD);

        JLabel userLabel = new JLabel("👤  " + currentUser.getUsername());
        userLabel.setFont(UITheme.FONT_BODY);
        userLabel.setForeground(UITheme.TEXT_WHITE);
        userInfo.add(userLabel);

        JLabel roleLabel = new JLabel("Customer");
        roleLabel.setFont(UITheme.FONT_SMALL);
        roleLabel.setForeground(UITheme.ACCENT_BLUE);
        roleLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.ACCENT_BLUE, 1),
            BorderFactory.createEmptyBorder(2, 8, 2, 8)
        ));
        userInfo.add(roleLabel);
        topBar.add(userInfo, BorderLayout.EAST);

        // WELCOME SECTION
        JPanel welcomePanel = new JPanel(new GridBagLayout());
        welcomePanel.setBackground(UITheme.BG_DARK);
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 10, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;

        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getUsername() + "! 👋", SwingConstants.CENTER);
        welcomeLabel.setFont(UITheme.FONT_TITLE);
        welcomeLabel.setForeground(UITheme.TEXT_WHITE);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 8, 0);
        welcomePanel.add(welcomeLabel, gbc);

        JLabel subLabel = new JLabel("Which car would you like to book today?", SwingConstants.CENTER);
        subLabel.setFont(UITheme.FONT_BODY);
        subLabel.setForeground(UITheme.TEXT_GRAY);
        gbc.gridy = 1;
        welcomePanel.add(subLabel, gbc);

        // MENU CARDS
        JPanel menuPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        menuPanel.setBackground(UITheme.BG_DARK);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));

        menuPanel.add(createMenuCard("🚗", "Book a Car", "Make a new booking", UITheme.ACCENT_BLUE));
        menuPanel.add(createMenuCard("📋", "My Bookings", "View your bookings", UITheme.ACCENT_GREEN));
        menuPanel.add(createMenuCard("👤", "My Profile", "View account details", UITheme.ACCENT_PURPLE));
        menuPanel.add(createMenuCard("🚪", "Logout", "Sign out of your account", UITheme.ACCENT_RED));

        // FOOTER
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(UITheme.BG_DARK);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        JLabel footerLabel = new JLabel("© 2024 Car Booking System — OOP Project");
        footerLabel.setFont(UITheme.FONT_SMALL);
        footerLabel.setForeground(UITheme.BORDER_COLOR);
        footerPanel.add(footerLabel);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(UITheme.BG_DARK);
        centerPanel.add(welcomePanel, BorderLayout.NORTH);
        centerPanel.add(menuPanel, BorderLayout.CENTER);
        centerPanel.add(footerPanel, BorderLayout.SOUTH);

        mainPanel.add(topBar, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JPanel createMenuCard(String emoji, String title, String desc, Color color) {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(UITheme.BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;

        JLabel iconLabel = new JLabel(emoji, SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        c.gridy = 0;
        c.insets = new Insets(0, 0, 10, 0);
        card.add(iconLabel, c);

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(UITheme.FONT_HEADING);
        titleLabel.setForeground(color);
        c.gridy = 1;
        c.insets = new Insets(0, 0, 5, 0);
        card.add(titleLabel, c);

        JLabel descLabel = new JLabel(desc, SwingConstants.CENTER);
        descLabel.setFont(UITheme.FONT_SMALL);
        descLabel.setForeground(UITheme.TEXT_GRAY);
        c.gridy = 2;
        c.insets = new Insets(0, 0, 0, 0);
        card.add(descLabel, c);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBackground(new Color(30, 40, 65));
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(color, 1),
                    BorderFactory.createEmptyBorder(20, 20, 20, 20)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBackground(UITheme.BG_CARD);
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1),
                    BorderFactory.createEmptyBorder(20, 20, 20, 20)
                ));
            }
            public void mouseClicked(java.awt.event.MouseEvent e) {
                handleMenuClick(title);
            }
        });

        return card;
    }

    private void handleMenuClick(String title) {
        switch (title) {
            case "Book a Car":
                new BookCarDialog(this, currentUser).setVisible(true);
                break;
            case "My Bookings":
                showMyBookings();
                break;
            case "My Profile":
                UITheme.showSuccess(this, "Username: " + currentUser.getUsername() +
                    "\nEmail: " + currentUser.getEmail() +
                    "\nCNIC: " + currentUser.getCnic() +
                    "\nRole: Customer");
                break;
            case "Logout":
                int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to logout?",
                    "Logout", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    new WelcomeScreen().setVisible(true);
                    this.dispose();
                }
                break;
        }
    }

    private void showMyBookings() {
        JDialog dialog = new JDialog(this, "My Bookings", true);
        dialog.setSize(750, 500);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(UITheme.BG_DARK);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UITheme.BG_DARK);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("📋  My Bookings");
        titleLabel.setFont(UITheme.FONT_TITLE);
        titleLabel.setForeground(UITheme.TEXT_WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        String[] columns = {"#", "Car", "Brand", "Start Date", "End Date", "Price/Day", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(model);
        table.setBackground(UITheme.BG_CARD);
        table.setForeground(UITheme.TEXT_WHITE);
        table.setFont(UITheme.FONT_BODY);
        table.setRowHeight(38);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.getTableHeader().setBackground(UITheme.BG_INPUT);
        table.getTableHeader().setForeground(UITheme.TEXT_GRAY);
        table.getTableHeader().setFont(UITheme.FONT_SMALL);
        table.setSelectionBackground(UITheme.ACCENT_BLUE);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                "SELECT b.id, ca.name, ca.brand, b.start_date, b.end_date, ca.price_per_day, b.status " +
                "FROM bookings b " +
                "JOIN cars ca ON b.car_id = ca.id " +
                "WHERE b.customer_id = ? " +
                "ORDER BY b.id DESC")) {
            stmt.setInt(1, currentUser.getId());
            ResultSet rs = stmt.executeQuery();
            boolean hasBookings = false;
            while (rs.next()) {
                hasBookings = true;
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("brand"),
                    rs.getString("start_date"),
                    rs.getString("end_date"),
                    "Rs. " + rs.getDouble("price_per_day"),
                    rs.getString("status")
                });
            }
            if (!hasBookings) {
                UITheme.showSuccess(dialog, "You have no bookings yet!");
                return;
            }
        } catch (Exception e) {
            UITheme.showError(this, "Error loading bookings:\n" + e.getMessage());
            return;
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBackground(UITheme.BG_CARD);
        scrollPane.setBorder(BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1));
        scrollPane.getViewport().setBackground(UITheme.BG_CARD);

        JButton closeBtn = UITheme.createButton("Close", UITheme.ACCENT_RED);
        closeBtn.setPreferredSize(new Dimension(160, 42));
        closeBtn.addActionListener(e -> dialog.dispose());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(UITheme.BG_DARK);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));
        bottomPanel.add(closeBtn);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        dialog.add(mainPanel);
        dialog.setVisible(true);
    }
}
