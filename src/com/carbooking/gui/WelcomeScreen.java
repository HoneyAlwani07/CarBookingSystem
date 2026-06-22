package com.carbooking.gui;

import javax.swing.*;
import java.awt.*;

public class WelcomeScreen extends JFrame {

    public WelcomeScreen() {
        setTitle("Car Booking System - Welcome");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(520, 620);
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
        headerPanel.setBorder(BorderFactory.createEmptyBorder(50, 20, 10, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;

        JLabel icon = new JLabel("🚗", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 70));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 15, 0);
        headerPanel.add(icon, gbc);

        JLabel titleLabel = new JLabel("Car Booking System", SwingConstants.CENTER);
        titleLabel.setFont(UITheme.FONT_TITLE);
        titleLabel.setForeground(UITheme.TEXT_WHITE);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 8, 0);
        headerPanel.add(titleLabel, gbc);

        JLabel subLabel = new JLabel("Book your car easily and quickly", SwingConstants.CENTER);
        subLabel.setFont(UITheme.FONT_BODY);
        subLabel.setForeground(UITheme.TEXT_GRAY);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        headerPanel.add(subLabel, gbc);

        // CARD
        JPanel cardWrapper = new JPanel(new GridBagLayout());
        cardWrapper.setBackground(UITheme.BG_DARK);
        cardWrapper.setBorder(BorderFactory.createEmptyBorder(30, 55, 20, 55));

        JPanel card = UITheme.createCard();
        card.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;

        JButton loginBtn = UITheme.createButton("🔐   Login", UITheme.ACCENT_BLUE);
        loginBtn.setPreferredSize(new Dimension(290, 54));
        c.gridy = 0;
        c.insets = new Insets(0, 0, 12, 0);
        card.add(loginBtn, c);

        JLabel loginDesc = new JLabel("Already have an account? Login here", SwingConstants.CENTER);
        loginDesc.setFont(UITheme.FONT_SMALL);
        loginDesc.setForeground(UITheme.TEXT_GRAY);
        c.gridy = 1;
        c.insets = new Insets(0, 0, 25, 0);
        card.add(loginDesc, c);

        JSeparator sep = new JSeparator();
        sep.setForeground(UITheme.BORDER_COLOR);
        c.gridy = 2;
        c.insets = new Insets(0, 0, 25, 0);
        card.add(sep, c);

        JButton signupBtn = UITheme.createButton("📝   Sign Up", UITheme.ACCENT_GREEN);
        signupBtn.setPreferredSize(new Dimension(290, 54));
        c.gridy = 3;
        c.insets = new Insets(0, 0, 12, 0);
        card.add(signupBtn, c);

        JLabel signupDesc = new JLabel("New user? Create your account here", SwingConstants.CENTER);
        signupDesc.setFont(UITheme.FONT_SMALL);
        signupDesc.setForeground(UITheme.TEXT_GRAY);
        c.gridy = 4;
        c.insets = new Insets(0, 0, 0, 0);
        card.add(signupDesc, c);

        cardWrapper.add(card);

        // FOOTER
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(UITheme.BG_DARK);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        JLabel footerLabel = new JLabel("Honey 2nd Semester Final OOP Project");
        footerLabel.setFont(UITheme.FONT_SMALL);
        footerLabel.setForeground(UITheme.BORDER_COLOR);
        footerPanel.add(footerLabel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(cardWrapper, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        add(mainPanel);

        loginBtn.addActionListener(e -> {
            new LoginChoiceScreen(this).setVisible(true);
            this.setVisible(false);
        });

        signupBtn.addActionListener(e -> {
            new SignupChoiceScreen(this).setVisible(true);
            this.setVisible(false);
        });
    }
}
