package com.carbooking.gui;

import javax.swing.*;
import java.awt.*;

public class LoginChoiceScreen extends JFrame {

    private JFrame previousScreen;

    public LoginChoiceScreen(JFrame previous) {
        this.previousScreen = previous;
        setTitle("Car Booking System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(520, 580);
        setLocationRelativeTo(null);
        setResizable(false);
        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UITheme.BG_DARK);

        JPanel headerPanel = new JPanel(new GridBagLayout());
        headerPanel.setBackground(UITheme.BG_DARK);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(45, 20, 10, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;

        JLabel icon = new JLabel("🔐", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 55));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 10, 0);
        headerPanel.add(icon, gbc);

        JLabel titleLabel = new JLabel("Login", SwingConstants.CENTER);
        titleLabel.setFont(UITheme.FONT_TITLE);
        titleLabel.setForeground(UITheme.TEXT_WHITE);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 6, 0);
        headerPanel.add(titleLabel, gbc);

        JLabel subLabel = new JLabel("Choose Your Account Type", SwingConstants.CENTER);
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

        JButton customerBtn = UITheme.createButton("👤   Customer Login", UITheme.ACCENT_BLUE);
        customerBtn.setPreferredSize(new Dimension(290, 54));
        c.gridy = 0;
        c.insets = new Insets(0, 0, 10, 0);
        card.add(customerBtn, c);

        JLabel customerDesc = new JLabel("Login as a Customer", SwingConstants.CENTER);
        customerDesc.setFont(UITheme.FONT_SMALL);
        customerDesc.setForeground(UITheme.TEXT_GRAY);
        c.gridy = 1;
        c.insets = new Insets(0, 0, 28, 0);
        card.add(customerDesc, c);

        JSeparator sep = new JSeparator();
        sep.setForeground(UITheme.BORDER_COLOR);
        c.gridy = 2;
        c.insets = new Insets(0, 0, 28, 0);
        card.add(sep, c);

        JButton adminBtn = UITheme.createButton("🛡️   Admin Login", UITheme.ACCENT_ORANGE);
        adminBtn.setPreferredSize(new Dimension(290, 54));
        c.gridy = 3;
        c.insets = new Insets(0, 0, 10, 0);
        card.add(adminBtn, c);

        JLabel adminDesc = new JLabel("Login as an Admin", SwingConstants.CENTER);
        adminDesc.setFont(UITheme.FONT_SMALL);
        adminDesc.setForeground(UITheme.TEXT_GRAY);
        c.gridy = 4;
        c.insets = new Insets(0, 0, 0, 0);
        card.add(adminDesc, c);

        cardWrapper.add(card);

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

        customerBtn.addActionListener(e -> {
            new LoginScreen("customer", this).setVisible(true);
            this.setVisible(false);
        });

        adminBtn.addActionListener(e -> {
            new LoginScreen("admin", this).setVisible(true);
            this.setVisible(false);
        });

        backBtn.addActionListener(e -> {
            if (previousScreen != null) {
                previousScreen.setVisible(true);
            }
            this.dispose();
        });
    }
}
