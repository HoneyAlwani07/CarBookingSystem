package com.carbooking.gui;

import javax.swing.*;
import java.awt.*;

public class SignupSuccessScreen extends JFrame {

    private JFrame previousScreen;

    public SignupSuccessScreen(String username, String role, JFrame previous) {
        this.previousScreen = previous;
        setTitle("Car Booking System - Sign Up Successful!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(520, 550);
        setLocationRelativeTo(null);
        setResizable(false);
        initUI(username, role);
    }

    private void initUI(String username, String role) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UITheme.BG_DARK);

        JPanel headerPanel = new JPanel(new GridBagLayout());
        headerPanel.setBackground(UITheme.BG_DARK);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(45, 20, 10, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;

        JLabel icon = new JLabel("🎉", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 65));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 10, 0);
        headerPanel.add(icon, gbc);

        JLabel titleLabel = new JLabel("Congratulations!", SwingConstants.CENTER);
        titleLabel.setFont(UITheme.FONT_TITLE);
        titleLabel.setForeground(UITheme.ACCENT_GREEN);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 6, 0);
        headerPanel.add(titleLabel, gbc);

        JLabel subLabel = new JLabel("Your account has been created successfully!", SwingConstants.CENTER);
        subLabel.setFont(UITheme.FONT_BODY);
        subLabel.setForeground(UITheme.TEXT_GRAY);
        gbc.gridy = 2;
        headerPanel.add(subLabel, gbc);

        JPanel cardWrapper = new JPanel(new GridBagLayout());
        cardWrapper.setBackground(UITheme.BG_DARK);
        cardWrapper.setBorder(BorderFactory.createEmptyBorder(20, 55, 20, 55));

        JPanel card = UITheme.createCard();
        card.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel nameLabel = new JLabel("Username: " + username, SwingConstants.CENTER);
        nameLabel.setFont(UITheme.FONT_HEADING);
        nameLabel.setForeground(UITheme.TEXT_WHITE);
        c.gridy = 0;
        c.insets = new Insets(0, 0, 8, 0);
        card.add(nameLabel, c);

        String roleText = role.equals("admin") ? "🛡️ Admin Account" : "👤 Customer Account";
        JLabel roleLabel = new JLabel(roleText, SwingConstants.CENTER);
        roleLabel.setFont(UITheme.FONT_BODY);
        roleLabel.setForeground(role.equals("admin") ? UITheme.ACCENT_ORANGE : UITheme.ACCENT_BLUE);
        c.gridy = 1;
        c.insets = new Insets(0, 0, 30, 0);
        card.add(roleLabel, c);

        JSeparator sep = new JSeparator();
        sep.setForeground(UITheme.BORDER_COLOR);
        c.gridy = 2;
        c.insets = new Insets(0, 0, 25, 0);
        card.add(sep, c);

        JButton loginBtn = UITheme.createButton("🔐   Login Now", UITheme.ACCENT_BLUE);
        loginBtn.setPreferredSize(new Dimension(290, 54));
        c.gridy = 3;
        c.insets = new Insets(0, 0, 12, 0);
        card.add(loginBtn, c);

        JButton homeBtn = UITheme.createButton("🏠   Go to Home", UITheme.ACCENT_PURPLE);
        homeBtn.setPreferredSize(new Dimension(290, 54));
        c.gridy = 4;
        c.insets = new Insets(0, 0, 0, 0);
        card.add(homeBtn, c);

        cardWrapper.add(card);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(cardWrapper, BorderLayout.CENTER);
        add(mainPanel);

        loginBtn.addActionListener(e -> {
            new LoginChoiceScreen(null).setVisible(true);
            this.dispose();
        });

        homeBtn.addActionListener(e -> {
            previousScreen.setVisible(true);
            this.dispose();
        });
    }
}
