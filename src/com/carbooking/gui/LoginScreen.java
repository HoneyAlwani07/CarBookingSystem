package com.carbooking.gui;

import com.carbooking.db.UserDAO;
import com.carbooking.model.User;
import javax.swing.*;
import java.awt.*;

public class LoginScreen extends JFrame {

    private JFrame previousScreen;
    private String role;
    private JTextField cnicField;
    private JPasswordField passwordField;

    public LoginScreen(String role, JFrame previous) {
        this.role = role;
        this.previousScreen = previous;
        setTitle("Car Booking System - Login (" + role + ")");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(520, 580);
        setLocationRelativeTo(null);
        setResizable(false);
        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UITheme.BG_DARK);

        // HEADER
        JPanel headerPanel = new JPanel(new GridBagLayout());
        headerPanel.setBackground(UITheme.BG_DARK);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(45, 20, 10, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;

        String iconText = role.equals("admin") ? "🛡️" : "👤";
        JLabel icon = new JLabel(iconText, SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 55));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 10, 0);
        headerPanel.add(icon, gbc);

        JLabel titleLabel = new JLabel("Login — " + (role.equals("admin") ? "Admin" : "Customer"), SwingConstants.CENTER);
        titleLabel.setFont(UITheme.FONT_TITLE);
        titleLabel.setForeground(UITheme.TEXT_WHITE);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 6, 0);
        headerPanel.add(titleLabel, gbc);

        JLabel subLabel = new JLabel("Enter your CNIC and Password to login", SwingConstants.CENTER);
        subLabel.setFont(UITheme.FONT_BODY);
        subLabel.setForeground(UITheme.TEXT_GRAY);
        gbc.gridy = 2;
        headerPanel.add(subLabel, gbc);

        // CARD
        JPanel cardWrapper = new JPanel(new GridBagLayout());
        cardWrapper.setBackground(UITheme.BG_DARK);
        cardWrapper.setBorder(BorderFactory.createEmptyBorder(20, 45, 10, 45));

        JPanel card = UITheme.createCard();
        card.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;

        JLabel cnicLabel = UITheme.createLabel("CNIC (xxxxx-xxxxxxx-x):");
        c.gridy = 0;
        c.insets = new Insets(0, 0, 4, 0);
        card.add(cnicLabel, c);

        cnicField = UITheme.createTextField("Enter CNIC");
        c.gridy = 1;
        c.insets = new Insets(0, 0, 15, 0);
        card.add(cnicField, c);

        JLabel passLabel = UITheme.createLabel("Password:");
        c.gridy = 2;
        c.insets = new Insets(0, 0, 4, 0);
        card.add(passLabel, c);

        passwordField = UITheme.createPasswordField();
        c.gridy = 3;
        c.insets = new Insets(0, 0, 25, 0);
        card.add(passwordField, c);

        Color btnColor = role.equals("admin") ? UITheme.ACCENT_ORANGE : UITheme.ACCENT_BLUE;
        JButton loginBtn = UITheme.createButton("🔐   Login", btnColor);
        loginBtn.setPreferredSize(new Dimension(290, 54));
        c.gridy = 4;
        c.insets = new Insets(0, 0, 0, 0);
        card.add(loginBtn, c);

        cardWrapper.add(card);

        // BACK BUTTON
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(UITheme.BG_DARK);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 20, 0));
        JButton backBtn = new JButton("← Go Back");
        backBtn.setFont(UITheme.FONT_BODY);
        backBtn.setBackground(UITheme.BG_CARD);
        backBtn.setForeground(UITheme.TEXT_GRAY);
        backBtn.setFocusPainted(false);
        backBtn.setBorderPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bottomPanel.add(backBtn);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(cardWrapper, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(mainPanel);

        loginBtn.addActionListener(e -> handleLogin());
        backBtn.addActionListener(e -> {
            previousScreen.setVisible(true);
            this.dispose();
        });
    }

    private void handleLogin() {
        String cnic     = cnicField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (cnic.isEmpty() || password.isEmpty()) {
            UITheme.showError(this, "CNIC and Password are both required!");
            return;
        }
        if (!cnic.matches("\\d{5}-\\d{7}-\\d{1}")) {
            UITheme.showError(this, "Invalid CNIC format!\nCorrect format: xxxxx-xxxxxxx-x");
            return;
        }

        UserDAO dao = new UserDAO();
        User user = role.equals("admin") ? dao.loginAdmin(cnic, password) : dao.loginCustomer(cnic, password);

        if (user != null) {
            if (role.equals("admin")) {
                new AdminDashboard(user, this).setVisible(true);
            } else {
                new CustomerDashboard(user, this).setVisible(true);
            }
            this.setVisible(false);
        } else {
            UITheme.showError(this, "Incorrect CNIC or Password!\nPlease try again.");
        }
    }
}
