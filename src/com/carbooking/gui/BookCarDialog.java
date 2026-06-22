package com.carbooking.gui;

import com.carbooking.model.User;
import com.carbooking.db.DatabaseConnection;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

public class BookCarDialog extends JDialog {

    private User currentUser;
    private JTable carTable;
    private DefaultTableModel tableModel;
    private JTextField startDateField;
    private JTextField endDateField;

    public BookCarDialog(JFrame parent, User user) {
        super(parent, "Book a Car", true);
        this.currentUser = user;
        setSize(750, 600);
        setLocationRelativeTo(parent);
        setResizable(false);
        initUI();
        loadCars();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UITheme.BG_DARK);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // HEADER
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UITheme.BG_DARK);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JLabel titleLabel = new JLabel("Available Cars");
        titleLabel.setFont(UITheme.FONT_TITLE);
        titleLabel.setForeground(UITheme.TEXT_WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JLabel subLabel = new JLabel("Select a car and book it");
        subLabel.setFont(UITheme.FONT_BODY);
        subLabel.setForeground(UITheme.TEXT_GRAY);
        headerPanel.add(subLabel, BorderLayout.SOUTH);

        // TABLE
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(UITheme.BG_CARD);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        String[] columns = {"#", "Car Name", "Brand", "Model Year", "Price/Day", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        carTable = new JTable(tableModel);
        carTable.setBackground(UITheme.BG_CARD);
        carTable.setForeground(UITheme.TEXT_WHITE);
        carTable.setFont(UITheme.FONT_BODY);
        carTable.setRowHeight(38);
        carTable.setShowGrid(false);
        carTable.setIntercellSpacing(new Dimension(0, 0));
        carTable.setSelectionBackground(new Color(41, 128, 255, 80));
        carTable.setSelectionForeground(UITheme.TEXT_WHITE);
        carTable.getTableHeader().setBackground(UITheme.BG_INPUT);
        carTable.getTableHeader().setForeground(UITheme.TEXT_GRAY);
        carTable.getTableHeader().setFont(UITheme.FONT_SMALL);
        carTable.getTableHeader().setReorderingAllowed(false);
        carTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        carTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        carTable.getColumnModel().getColumn(1).setPreferredWidth(180);
        carTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        carTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        carTable.getColumnModel().getColumn(4).setPreferredWidth(110);
        carTable.getColumnModel().getColumn(5).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(carTable);
        scrollPane.setBackground(UITheme.BG_CARD);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(UITheme.BG_CARD);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // BOOKING FORM
        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        formPanel.setBackground(UITheme.BG_CARD);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JLabel startLabel = UITheme.createLabel("Start Date:");
        formPanel.add(startLabel);

        startDateField = new JTextField("2024-01-01");
        startDateField.setFont(UITheme.FONT_BODY);
        startDateField.setBackground(UITheme.BG_INPUT);
        startDateField.setForeground(UITheme.TEXT_WHITE);
        startDateField.setCaretColor(UITheme.TEXT_WHITE);
        startDateField.setPreferredSize(new Dimension(130, 40));
        startDateField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(startDateField);

        JLabel endLabel = UITheme.createLabel("End Date:");
        formPanel.add(endLabel);

        endDateField = new JTextField("2024-01-07");
        endDateField.setFont(UITheme.FONT_BODY);
        endDateField.setBackground(UITheme.BG_INPUT);
        endDateField.setForeground(UITheme.TEXT_WHITE);
        endDateField.setCaretColor(UITheme.TEXT_WHITE);
        endDateField.setPreferredSize(new Dimension(130, 40));
        endDateField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(endDateField);

        JButton bookBtn = UITheme.createButton("Book Now", UITheme.ACCENT_GREEN);
        bookBtn.setPreferredSize(new Dimension(150, 40));
        formPanel.add(bookBtn);

        // BOTTOM BUTTONS
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bottomPanel.setBackground(UITheme.BG_DARK);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));

        JButton refreshBtn = UITheme.createButton("Refresh", UITheme.ACCENT_PURPLE);
        refreshBtn.setPreferredSize(new Dimension(130, 42));
        bottomPanel.add(refreshBtn);

        JButton closeBtn = UITheme.createButton("Close", UITheme.ACCENT_RED);
        closeBtn.setPreferredSize(new Dimension(150, 42));
        bottomPanel.add(closeBtn);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBackground(UITheme.BG_DARK);
        southPanel.add(formPanel, BorderLayout.NORTH);
        southPanel.add(bottomPanel, BorderLayout.SOUTH);
        mainPanel.add(southPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // ACTIONS
        bookBtn.addActionListener(e -> {
            int selectedRow = carTable.getSelectedRow();
            if (selectedRow == -1) {
                UITheme.showError(this, "Please select a car first!");
                return;
            }
            String status = (String) tableModel.getValueAt(selectedRow, 5);
            if (!status.equals("Available")) {
                UITheme.showError(this, "This car is already booked!");
                return;
            }
            String startDate = startDateField.getText().trim();
            String endDate = endDateField.getText().trim();
            if (startDate.isEmpty() || endDate.isEmpty()) {
                UITheme.showError(this, "Start and End date are required!");
                return;
            }
            if (!startDate.matches("\\d{4}-\\d{2}-\\d{2}") ||
                !endDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                UITheme.showError(this, "Invalid date format!\nCorrect format: YYYY-MM-DD");
                return;
            }
            int carId = (int) tableModel.getValueAt(selectedRow, 0);
            String carName = (String) tableModel.getValueAt(selectedRow, 1);
            handleBooking(carId, carName, startDate, endDate);
        });

        refreshBtn.addActionListener(e -> loadCars());
        closeBtn.addActionListener(e -> dispose());
    }

    private void loadCars() {
        tableModel.setRowCount(0);
        // Show all available cars from all admins
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM cars ORDER BY id")) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("brand"),
                    rs.getInt("year"),
                    "Rs. " + rs.getDouble("price_per_day"),
                    rs.getString("status")
                });
            }
        } catch (Exception e) {
            UITheme.showError(this, "Error loading cars:\n" + e.getMessage());
        }
    }

    private void handleBooking(int carId, String carName, String startDate, String endDate) {
        // Get the admin_id from the car being booked
        try (Connection conn = DatabaseConnection.getConnection()) {
            int adminId;
            try (PreparedStatement getAdmin = conn.prepareStatement(
                    "SELECT admin_id FROM cars WHERE id = ?")) {
                getAdmin.setInt(1, carId);
                ResultSet rs = getAdmin.executeQuery();
                if (!rs.next()) {
                    UITheme.showError(this, "Car not found!");
                    return;
                }
                adminId = rs.getInt("admin_id");
            }

            String sql = "INSERT INTO bookings (admin_id, customer_id, car_id, start_date, end_date, status) " +
                         "VALUES (?, ?, ?, ?, ?, 'Pending')";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, adminId);
                stmt.setInt(2, currentUser.getId());
                stmt.setInt(3, carId);
                stmt.setString(4, startDate);
                stmt.setString(5, endDate);
                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    try (PreparedStatement upd = conn.prepareStatement(
                            "UPDATE cars SET status = 'Booked' WHERE id = ?")) {
                        upd.setInt(1, carId);
                        upd.executeUpdate();
                    }
                    UITheme.showSuccess(this,
                        "Booking Successful!\n\n" +
                        "Car: " + carName + "\n" +
                        "Start: " + startDate + "\n" +
                        "End: " + endDate + "\n" +
                        "Status: Pending");
                    loadCars();
                }
            }
        } catch (Exception e) {
            UITheme.showError(this, "Booking error:\n" + e.getMessage());
        }
    }
}
