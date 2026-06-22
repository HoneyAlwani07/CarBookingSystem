package com.carbooking.gui;

import com.carbooking.model.User;
import com.carbooking.db.DatabaseConnection;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

public class AdminDashboard extends JFrame {

    private User currentUser;
    private JFrame previousScreen;

    public AdminDashboard(User user, JFrame previous) {
        this.currentUser = user;
        this.previousScreen = previous;
        setTitle("Car Booking System - Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
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

        JLabel titleLabel = new JLabel("🚗  Car Booking System — Admin Panel");
        titleLabel.setFont(UITheme.FONT_HEADING);
        titleLabel.setForeground(UITheme.ACCENT_ORANGE);
        topBar.add(titleLabel, BorderLayout.WEST);

        JPanel userInfo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        userInfo.setBackground(UITheme.BG_CARD);

        JLabel userLabel = new JLabel("🛡️  " + currentUser.getUsername());
        userLabel.setFont(UITheme.FONT_BODY);
        userLabel.setForeground(UITheme.TEXT_WHITE);
        userInfo.add(userLabel);

        JLabel roleLabel = new JLabel("Admin");
        roleLabel.setFont(UITheme.FONT_SMALL);
        roleLabel.setForeground(UITheme.ACCENT_ORANGE);
        roleLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.ACCENT_ORANGE, 1),
            BorderFactory.createEmptyBorder(2, 8, 2, 8)
        ));
        userInfo.add(roleLabel);
        topBar.add(userInfo, BorderLayout.EAST);

        // SIDE PANEL
        JPanel sidePanel = new JPanel(new GridBagLayout());
        sidePanel.setBackground(UITheme.BG_CARD);
        sidePanel.setPreferredSize(new Dimension(220, 0));
        sidePanel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        GridBagConstraints sc = new GridBagConstraints();
        sc.gridx = 0;
        sc.fill = GridBagConstraints.HORIZONTAL;
        sc.weightx = 1.0;

        JLabel menuTitle = new JLabel("MENU", SwingConstants.LEFT);
        menuTitle.setFont(UITheme.FONT_SMALL);
        menuTitle.setForeground(UITheme.TEXT_GRAY);
        menuTitle.setBorder(BorderFactory.createEmptyBorder(0, 5, 15, 0));
        sc.gridy = 0;
        sc.insets = new Insets(0, 0, 5, 0);
        sidePanel.add(menuTitle, sc);

        String[][] menuItems = {
            {"📊", "Dashboard"},
            {"🚗", "Manage Cars"},
            {"📋", "All Bookings"},
            {"👥", "Customers"},
            {"➕", "Add New Car"},
            {"🚪", "Logout"}
        };

        for (int i = 0; i < menuItems.length; i++) {
            JButton menuBtn = createSideButton(menuItems[i][0] + "  " + menuItems[i][1]);
            sc.gridy = i + 1;
            sc.insets = new Insets(0, 0, 8, 0);
            final String btnTitle = menuItems[i][1];
            menuBtn.addActionListener(e -> handleSideMenu(btnTitle));
            sidePanel.add(menuBtn, sc);
        }

        sc.gridy = menuItems.length + 1;
        sc.weighty = 1.0;
        sidePanel.add(new JLabel(), sc);

        // MAIN CONTENT
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(UITheme.BG_DARK);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // STATS ROW
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        statsPanel.setBackground(UITheme.BG_DARK);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        statsPanel.add(createStatCard("🚗", "Total Cars", getCarCount(), UITheme.ACCENT_BLUE));
        statsPanel.add(createStatCard("📋", "Total Bookings", getBookingCount(), UITheme.ACCENT_GREEN));
        statsPanel.add(createStatCard("👥", "Total Customers", getCustomerCount(), UITheme.ACCENT_PURPLE));

        // BOOKINGS TABLE
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(UITheme.BG_CARD);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel tableTitle = new JLabel("📋  Recent Bookings");
        tableTitle.setFont(UITheme.FONT_HEADING);
        tableTitle.setForeground(UITheme.TEXT_WHITE);
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        tablePanel.add(tableTitle, BorderLayout.NORTH);

        String[] columns = {"#", "Customer", "Car", "Start Date", "End Date", "Status"};
        Object[][] data = getBookingsData();

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(model);
        table.setBackground(UITheme.BG_CARD);
        table.setForeground(UITheme.TEXT_WHITE);
        table.setFont(UITheme.FONT_BODY);
        table.setRowHeight(35);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.getTableHeader().setBackground(UITheme.BG_INPUT);
        table.getTableHeader().setForeground(UITheme.TEXT_GRAY);
        table.getTableHeader().setFont(UITheme.FONT_SMALL);
        table.setSelectionBackground(UITheme.ACCENT_BLUE);

        // APPROVE / REJECT BUTTONS
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        actionPanel.setBackground(UITheme.BG_CARD);

        JButton approveBtn = UITheme.createButton("✅  Approve", UITheme.ACCENT_GREEN);
        approveBtn.setPreferredSize(new Dimension(140, 40));
        JButton rejectBtn = UITheme.createButton("❌  Reject", UITheme.ACCENT_RED);
        rejectBtn.setPreferredSize(new Dimension(140, 40));

        actionPanel.add(approveBtn);
        actionPanel.add(rejectBtn);

        approveBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                UITheme.showError(this, "Please select a booking first!");
                return;
            }
            int bookingId = (int) model.getValueAt(selectedRow, 0);
            String status = (String) model.getValueAt(selectedRow, 5);
            if (status.equals("Confirmed")) {
                UITheme.showError(this, "This booking is already approved!");
                return;
            }
            updateBookingStatus(bookingId, "Confirmed");
            model.setValueAt("Confirmed", selectedRow, 5);
            UITheme.showSuccess(this, "Booking #" + bookingId + " has been approved!");
        });

        rejectBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                UITheme.showError(this, "Please select a booking first!");
                return;
            }
            int bookingId = (int) model.getValueAt(selectedRow, 0);
            String status = (String) model.getValueAt(selectedRow, 5);
            if (status.equals("Cancelled")) {
                UITheme.showError(this, "This booking has already been rejected!");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to reject booking #" + bookingId + "?",
                "Reject Booking", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                updateBookingStatus(bookingId, "Cancelled");
                updateCarStatus(bookingId);
                model.setValueAt("Cancelled", selectedRow, 5);
                UITheme.showSuccess(this, "Booking #" + bookingId + " has been rejected!");
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBackground(UITheme.BG_CARD);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(UITheme.BG_CARD);

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.add(actionPanel, BorderLayout.SOUTH);

        contentPanel.add(statsPanel, BorderLayout.NORTH);
        contentPanel.add(tablePanel, BorderLayout.CENTER);

        JPanel centerLayout = new JPanel(new BorderLayout());
        centerLayout.add(sidePanel, BorderLayout.WEST);
        centerLayout.add(contentPanel, BorderLayout.CENTER);

        mainPanel.add(topBar, BorderLayout.NORTH);
        mainPanel.add(centerLayout, BorderLayout.CENTER);
        add(mainPanel);
    }

    private void updateBookingStatus(int bookingId, String status) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                "UPDATE bookings SET status = ? WHERE id = ? AND admin_id = ?")) {
            stmt.setString(1, status);
            stmt.setInt(2, bookingId);
            stmt.setInt(3, currentUser.getId());
            stmt.executeUpdate();
        } catch (Exception e) {
            UITheme.showError(this, "Error updating status:\n" + e.getMessage());
        }
    }

    private void updateCarStatus(int bookingId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                "UPDATE cars SET status = 'Available' WHERE id = " +
                "(SELECT car_id FROM bookings WHERE id = ? AND admin_id = ?)")) {
            stmt.setInt(1, bookingId);
            stmt.setInt(2, currentUser.getId());
            stmt.executeUpdate();
        } catch (Exception e) {
            UITheme.showError(this, "Error updating car status:\n" + e.getMessage());
        }
    }

    private JButton createSideButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(UITheme.FONT_BODY);
        btn.setBackground(UITheme.BG_DARK);
        btn.setForeground(UITheme.TEXT_WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(UITheme.BG_INPUT);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(UITheme.BG_DARK);
            }
        });
        return btn;
    }

    private JPanel createStatCard(String emoji, String label, String value, Color color) {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(UITheme.BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;

        JLabel iconLabel = new JLabel(emoji, SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 30));
        c.gridy = 0;
        c.insets = new Insets(0, 0, 8, 0);
        card.add(iconLabel, c);

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(color);
        c.gridy = 1;
        c.insets = new Insets(0, 0, 4, 0);
        card.add(valueLabel, c);

        JLabel labelComp = new JLabel(label, SwingConstants.CENTER);
        labelComp.setFont(UITheme.FONT_SMALL);
        labelComp.setForeground(UITheme.TEXT_GRAY);
        c.gridy = 2;
        card.add(labelComp, c);

        return card;
    }

    // Stats queries filtered by admin_id
    private String getCarCount() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM cars WHERE admin_id = ?")) {
            stmt.setInt(1, currentUser.getId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return String.valueOf(rs.getInt(1));
        } catch (Exception e) { return "0"; }
        return "0";
    }

    private String getBookingCount() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM bookings WHERE admin_id = ?")) {
            stmt.setInt(1, currentUser.getId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return String.valueOf(rs.getInt(1));
        } catch (Exception e) { return "0"; }
        return "0";
    }

    private String getCustomerCount() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                "SELECT COUNT(DISTINCT customer_id) FROM bookings WHERE admin_id = ?")) {
            stmt.setInt(1, currentUser.getId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return String.valueOf(rs.getInt(1));
        } catch (Exception e) { return "0"; }
        return "0";
    }

    private Object[][] getBookingsData() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                "SELECT b.id, c.username, ca.name, b.start_date, b.end_date, b.status " +
                "FROM bookings b " +
                "JOIN customers c ON b.customer_id = c.id " +
                "JOIN cars ca ON b.car_id = ca.id " +
                "WHERE b.admin_id = ? " +
                "ORDER BY b.id DESC LIMIT 10")) {
            stmt.setInt(1, currentUser.getId());
            ResultSet rs = stmt.executeQuery();
            java.util.List<Object[]> rows = new java.util.ArrayList<>();
            while (rs.next()) {
                rows.add(new Object[]{
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("name"),
                    rs.getString("start_date"),
                    rs.getString("end_date"),
                    rs.getString("status")
                });
            }
            return rows.toArray(new Object[0][]);
        } catch (Exception e) {
            return new Object[][]{};
        }
    }

    private void handleSideMenu(String title) {
        switch (title) {
            case "Dashboard":
                UITheme.showSuccess(this, "You are already on the Dashboard!");
                break;
            case "Manage Cars":
                showCarsManagement();
                break;
            case "All Bookings":
                showAllBookings();
                break;
            case "Customers":
                showAllCustomers();
                break;
            case "Add New Car":
                showAddCarDialog();
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

    // ============ MANAGE CARS ============
    private void showCarsManagement() {
        JDialog dialog = new JDialog(this, "Manage Cars", true);
        dialog.setSize(800, 500);
        dialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UITheme.BG_DARK);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("🚗  Manage Cars");
        titleLabel.setFont(UITheme.FONT_TITLE);
        titleLabel.setForeground(UITheme.TEXT_WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        String[] columns = {"#", "Name", "Brand", "Year", "Price/Day", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(model);
        table.setBackground(UITheme.BG_CARD);
        table.setForeground(UITheme.TEXT_WHITE);
        table.setFont(UITheme.FONT_BODY);
        table.setRowHeight(35);
        table.setShowGrid(false);
        table.getTableHeader().setBackground(UITheme.BG_INPUT);
        table.getTableHeader().setForeground(UITheme.TEXT_GRAY);
        table.setSelectionBackground(UITheme.ACCENT_BLUE);

        // Only load THIS admin's cars
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM cars WHERE admin_id = ? ORDER BY id")) {
            stmt.setInt(1, currentUser.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("brand"),
                    rs.getInt("year"),
                    "Rs. " + rs.getDouble("price_per_day"),
                    rs.getString("status")
                });
            }
        } catch (Exception e) {
            UITheme.showError(this, "Error loading cars!");
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(UITheme.BG_CARD);
        scrollPane.setBorder(BorderFactory.createLineBorder(UITheme.BORDER_COLOR));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnPanel.setBackground(UITheme.BG_DARK);

        JButton deleteBtn = UITheme.createButton("🗑️  Delete Car", UITheme.ACCENT_RED);
        deleteBtn.setPreferredSize(new Dimension(160, 40));
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) { UITheme.showError(dialog, "Please select a car first!"); return; }
            int carId = (int) model.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(dialog,
                "Are you sure you want to delete this car?",
                "Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(
                        "DELETE FROM cars WHERE id = ? AND admin_id = ?")) {
                    stmt.setInt(1, carId);
                    stmt.setInt(2, currentUser.getId());
                    stmt.executeUpdate();
                    model.removeRow(row);
                    UITheme.showSuccess(dialog, "Car deleted successfully!");
                } catch (Exception ex) {
                    UITheme.showError(dialog, "Error deleting car:\n" + ex.getMessage());
                }
            }
        });

        JButton closeBtn = UITheme.createButton("Close", UITheme.ACCENT_PURPLE);
        closeBtn.setPreferredSize(new Dimension(150, 40));
        closeBtn.addActionListener(e -> dialog.dispose());

        btnPanel.add(deleteBtn);
        btnPanel.add(closeBtn);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    // ============ ALL BOOKINGS ============
    private void showAllBookings() {
        JDialog dialog = new JDialog(this, "All Bookings", true);
        dialog.setSize(850, 500);
        dialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UITheme.BG_DARK);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("📋  All Bookings");
        titleLabel.setFont(UITheme.FONT_TITLE);
        titleLabel.setForeground(UITheme.TEXT_WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        String[] columns = {"#", "Customer", "Car", "Start Date", "End Date", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(model);
        table.setBackground(UITheme.BG_CARD);
        table.setForeground(UITheme.TEXT_WHITE);
        table.setFont(UITheme.FONT_BODY);
        table.setRowHeight(35);
        table.setShowGrid(false);
        table.getTableHeader().setBackground(UITheme.BG_INPUT);
        table.getTableHeader().setForeground(UITheme.TEXT_GRAY);
        table.setSelectionBackground(UITheme.ACCENT_BLUE);

        // Only load THIS admin's bookings
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                "SELECT b.id, c.username, ca.name, b.start_date, b.end_date, b.status " +
                "FROM bookings b " +
                "JOIN customers c ON b.customer_id = c.id " +
                "JOIN cars ca ON b.car_id = ca.id " +
                "WHERE b.admin_id = ? " +
                "ORDER BY b.id DESC")) {
            stmt.setInt(1, currentUser.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("name"),
                    rs.getString("start_date"),
                    rs.getString("end_date"),
                    rs.getString("status")
                });
            }
        } catch (Exception e) {
            UITheme.showError(this, "Error loading bookings!");
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(UITheme.BG_CARD);
        scrollPane.setBorder(BorderFactory.createLineBorder(UITheme.BORDER_COLOR));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnPanel.setBackground(UITheme.BG_DARK);

        JButton approveBtn = UITheme.createButton("✅  Approve", UITheme.ACCENT_GREEN);
        approveBtn.setPreferredSize(new Dimension(130, 40));
        approveBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) { UITheme.showError(dialog, "Please select a booking first!"); return; }
            int bookingId = (int) model.getValueAt(row, 0);
            updateBookingStatus(bookingId, "Confirmed");
            model.setValueAt("Confirmed", row, 5);
            UITheme.showSuccess(dialog, "Booking approved!");
        });

        JButton rejectBtn = UITheme.createButton("❌  Reject", UITheme.ACCENT_RED);
        rejectBtn.setPreferredSize(new Dimension(130, 40));
        rejectBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) { UITheme.showError(dialog, "Please select a booking first!"); return; }
            int bookingId = (int) model.getValueAt(row, 0);
            updateBookingStatus(bookingId, "Cancelled");
            updateCarStatus(bookingId);
            model.setValueAt("Cancelled", row, 5);
            UITheme.showSuccess(dialog, "Booking rejected!");
        });

        JButton closeBtn = UITheme.createButton("Close", UITheme.ACCENT_PURPLE);
        closeBtn.setPreferredSize(new Dimension(140, 40));
        closeBtn.addActionListener(e -> dialog.dispose());

        btnPanel.add(approveBtn);
        btnPanel.add(rejectBtn);
        btnPanel.add(closeBtn);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    // ============ CUSTOMERS LIST ============
    private void showAllCustomers() {
        JDialog dialog = new JDialog(this, "Customers", true);
        dialog.setSize(700, 450);
        dialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UITheme.BG_DARK);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("👥  Customers List");
        titleLabel.setFont(UITheme.FONT_TITLE);
        titleLabel.setForeground(UITheme.TEXT_WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        String[] columns = {"#", "Username", "CNIC", "Email", "Registered"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(model);
        table.setBackground(UITheme.BG_CARD);
        table.setForeground(UITheme.TEXT_WHITE);
        table.setFont(UITheme.FONT_BODY);
        table.setRowHeight(35);
        table.setShowGrid(false);
        table.getTableHeader().setBackground(UITheme.BG_INPUT);
        table.getTableHeader().setForeground(UITheme.TEXT_GRAY);
        table.setSelectionBackground(UITheme.ACCENT_BLUE);

        // Only show customers who have booked THIS admin's cars
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                "SELECT DISTINCT cu.id, cu.username, cu.cnic, cu.email, cu.created_at " +
                "FROM customers cu " +
                "JOIN bookings b ON cu.id = b.customer_id " +
                "WHERE b.admin_id = ? " +
                "ORDER BY cu.id DESC")) {
            stmt.setInt(1, currentUser.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("cnic"),
                    rs.getString("email"),
                    rs.getString("created_at")
                });
            }
        } catch (Exception e) {
            UITheme.showError(this, "Error loading customers!");
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(UITheme.BG_CARD);
        scrollPane.setBorder(BorderFactory.createLineBorder(UITheme.BORDER_COLOR));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnPanel.setBackground(UITheme.BG_DARK);

        JButton closeBtn = UITheme.createButton("Close", UITheme.ACCENT_PURPLE);
        closeBtn.setPreferredSize(new Dimension(150, 40));
        closeBtn.addActionListener(e -> dialog.dispose());
        btnPanel.add(closeBtn);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    // ============ ADD NEW CAR ============
    private void showAddCarDialog() {
        JDialog dialog = new JDialog(this, "Add New Car", true);
        dialog.setSize(450, 450);
        dialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UITheme.BG_DARK);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titleLabel = new JLabel("➕  Add New Car");
        titleLabel.setFont(UITheme.FONT_HEADING);
        titleLabel.setForeground(UITheme.TEXT_WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UITheme.BG_DARK);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0; c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;

        JTextField nameField = UITheme.createTextField("Car name");
        JTextField brandField = UITheme.createTextField("Brand (Toyota, Honda...)");
        JTextField yearField = UITheme.createTextField("Year (2023)");
        JTextField priceField = UITheme.createTextField("Price per day (Rs.)");

        c.gridy = 0; c.insets = new Insets(0, 0, 4, 0);
        formPanel.add(UITheme.createLabel("Car Name:"), c);
        c.gridy = 1; c.insets = new Insets(0, 0, 12, 0);
        formPanel.add(nameField, c);

        c.gridy = 2; c.insets = new Insets(0, 0, 4, 0);
        formPanel.add(UITheme.createLabel("Brand:"), c);
        c.gridy = 3; c.insets = new Insets(0, 0, 12, 0);
        formPanel.add(brandField, c);

        c.gridy = 4; c.insets = new Insets(0, 0, 4, 0);
        formPanel.add(UITheme.createLabel("Model Year:"), c);
        c.gridy = 5; c.insets = new Insets(0, 0, 12, 0);
        formPanel.add(yearField, c);

        c.gridy = 6; c.insets = new Insets(0, 0, 4, 0);
        formPanel.add(UITheme.createLabel("Price Per Day (Rs.):"), c);
        c.gridy = 7; c.insets = new Insets(0, 0, 20, 0);
        formPanel.add(priceField, c);

        JButton addBtn = UITheme.createButton("➕  Add Car", UITheme.ACCENT_GREEN);
        addBtn.setPreferredSize(new Dimension(250, 45));
        addBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String brand = brandField.getText().trim();
            String yearStr = yearField.getText().trim();
            String priceStr = priceField.getText().trim();

            if (name.isEmpty() || brand.isEmpty() || yearStr.isEmpty() || priceStr.isEmpty()) {
                UITheme.showError(dialog, "All fields are required!");
                return;
            }

            try {
                int year = Integer.parseInt(yearStr);
                double price = Double.parseDouble(priceStr);

                // Insert car with this admin's id
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO cars (admin_id, name, brand, year, price_per_day, status) VALUES (?, ?, ?, ?, ?, 'Available')")) {
                    stmt.setInt(1, currentUser.getId());
                    stmt.setString(2, name);
                    stmt.setString(3, brand);
                    stmt.setInt(4, year);
                    stmt.setDouble(5, price);
                    stmt.executeUpdate();
                    UITheme.showSuccess(dialog, "Car added successfully!");
                    dialog.dispose();
                }
            } catch (NumberFormatException ex) {
                UITheme.showError(dialog, "Year and Price must be numbers only!");
            } catch (Exception ex) {
                UITheme.showError(dialog, "Error adding car:\n" + ex.getMessage());
            }
        });

        c.gridy = 8; c.insets = new Insets(0, 0, 0, 0);
        c.anchor = GridBagConstraints.CENTER;
        formPanel.add(addBtn, c);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }
}
