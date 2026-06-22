package com.carbooking.gui;

import com.carbooking.db.UserDAO;
import com.carbooking.model.User;
import javax.swing.*;
import java.awt.*;

public class SignupScreen extends JFrame {

    private JFrame previousScreen;
    private String role;
    private JTextField usernameField, cnicField, emailField;
    private JPasswordField passwordField, confirmPasswordField;

    public SignupScreen(String role, JFrame previous) {
        this.role = role;
        this.previousScreen = previous;
        setTitle("Car Booking System - Sign Up (" + role + ")");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(520, 700);
        setLocationRelativeTo(null);
        setResizable(false);
        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UITheme.BG_DARK);

        JPanel headerPanel = new JPanel(new GridBagLayout());
        headerPanel.setBackground(UITheme.BG_DARK);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 10, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;

        String iconText = role.equals("admin") ? "🛡️" : "👤";
        JLabel icon = new JLabel(iconText, SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 8, 0);
        headerPanel.add(icon, gbc);

        JLabel titleLabel = new JLabel("Sign Up — " + (role.equals("admin") ? "Admin" : "Customer"), SwingConstants.CENTER);
        titleLabel.setFont(UITheme.FONT_TITLE);
        titleLabel.setForeground(UITheme.TEXT_WHITE);
        gbc.gridy = 1;
        headerPanel.add(titleLabel, gbc);

        JPanel cardWrapper = new JPanel(new GridBagLayout());
        cardWrapper.setBackground(UITheme.BG_DARK);
        cardWrapper.setBorder(BorderFactory.createEmptyBorder(15, 45, 10, 45));

        JPanel card = UITheme.createCard();
        card.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;

        card.add(UITheme.createLabel("Username:"), fieldLabel(c, 0));
        usernameField = UITheme.createTextField("Enter username");
        card.add(usernameField, fieldInput(c, 1));

        card.add(UITheme.createLabel("CNIC (xxxxx-xxxxxxx-x):"), fieldLabel(c, 2));
        cnicField = UITheme.createTextField("Enter CNIC");
        card.add(cnicField, fieldInput(c, 3));

        card.add(UITheme.createLabel("Email:"), fieldLabel(c, 4));
        emailField = UITheme.createTextField("Enter email");
        card.add(emailField, fieldInput(c, 5));

        card.add(UITheme.createLabel("Password:"), fieldLabel(c, 6));
        passwordField = UITheme.createPasswordField();
        card.add(passwordField, fieldInput(c, 7));

        card.add(UITheme.createLabel("Confirm Password:"), fieldLabel(c, 8));
        confirmPasswordField = UITheme.createPasswordField();
        card.add(confirmPasswordField, fieldInput(c, 9));

        Color btnColor = role.equals("admin") ? UITheme.ACCENT_ORANGE : UITheme.ACCENT_GREEN;
        JButton signupBtn = UITheme.createButton("✅   Create Account", btnColor);
        signupBtn.setPreferredSize(new Dimension(290, 50));
        c.gridy = 10;
        c.insets = new Insets(20, 0, 0, 0);
        card.add(signupBtn, c);

        cardWrapper.add(card);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(UITheme.BG_DARK);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));
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

        signupBtn.addActionListener(e -> handleSignup());
        backBtn.addActionListener(e -> {
            previousScreen.setVisible(true);
            this.dispose();
        });
    }

    private GridBagConstraints fieldLabel(GridBagConstraints c, int row) {
        c.gridy = row;
        c.insets = new Insets(row == 0 ? 0 : 12, 0, 4, 0);
        return c;
    }

    private GridBagConstraints fieldInput(GridBagConstraints c, int row) {
        c.gridy = row;
        c.insets = new Insets(0, 0, 0, 0);
        return c;
    }

    private void handleSignup() {
        String username = usernameField.getText().trim();
        String cnic     = cnicField.getText().trim();
        String email    = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String confirm  = new String(confirmPasswordField.getPassword()).trim();

        if (username.isEmpty() || cnic.isEmpty() || email.isEmpty() || password.isEmpty()) {
            UITheme.showError(this, "All fields are required!");
            return;
        }
        if (!cnic.matches("\\d{5}-\\d{7}-\\d{1}")) {
            UITheme.showError(this, "Invalid CNIC format!\nCorrect format: xxxxx-xxxxxxx-x");
            return;
        }
        if (!email.contains("@") || !email.contains(".")) {
            UITheme.showError(this, "Invalid email address!");
            return;
        }
        if (password.length() < 6) {
            UITheme.showError(this, "Password must be at least 6 characters!");
            return;
        }
        if (!password.equals(confirm)) {
            UITheme.showError(this, "Passwords do not match!");
            return;
        }

        UserDAO dao = new UserDAO();
        if (dao.isUsernameTaken(username, role)) {
            UITheme.showError(this, "This username is already taken!");
            return;
        }
        if (dao.isCnicTaken(cnic, role)) {
            UITheme.showError(this, "This CNIC is already registered!");
            return;
        }

        User user = new User(username, password, cnic, email, role);
        boolean success = role.equals("admin") ? dao.signupAdmin(user) : dao.signupCustomer(user);

        if (success) {
            new SignupSuccessScreen(username, role, this).setVisible(true);
            this.setVisible(false);
        } else {
            UITheme.showError(this, "Error creating account. Please try again!");
        }
    }
}
