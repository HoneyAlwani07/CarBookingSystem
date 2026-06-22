package com.carbooking;

import com.carbooking.gui.WelcomeScreen;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new WelcomeScreen().setVisible(true);
        });
    }
}
