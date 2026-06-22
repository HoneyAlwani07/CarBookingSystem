package com.carbooking.db;

import com.carbooking.model.User;
import java.sql.*;

public class UserDAO {

    public boolean signupCustomer(User user) {
        String sql = "INSERT INTO customers (username, password, cnic, email) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getCnic());
            stmt.setString(4, user.getEmail());
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLIntegrityConstraintViolationException e) {
            System.err.println("Duplicate entry: " + e.getMessage());
            return false;
        } catch (SQLException e) {
            System.err.println("Signup error: " + e.getMessage());
            return false;
        }
    }

    public boolean signupAdmin(User user) {
        String sql = "INSERT INTO admins (username, password, cnic, email) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getCnic());
            stmt.setString(4, user.getEmail());
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLIntegrityConstraintViolationException e) {
            System.err.println("Duplicate entry: " + e.getMessage());
            return false;
        } catch (SQLException e) {
            System.err.println("Admin signup error: " + e.getMessage());
            return false;
        }
    }

    public User loginCustomer(String cnic, String password) {
        String sql = "SELECT * FROM customers WHERE cnic = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cnic);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setCnic(rs.getString("cnic"));
                user.setEmail(rs.getString("email"));
                user.setRole("customer");
                return user;
            }
        } catch (SQLException e) {
            System.err.println("Login error: " + e.getMessage());
        }
        return null;
    }

    public User loginAdmin(String cnic, String password) {
        String sql = "SELECT * FROM admins WHERE cnic = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cnic);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setCnic(rs.getString("cnic"));
                user.setEmail(rs.getString("email"));
                user.setRole("admin");
                return user;
            }
        } catch (SQLException e) {
            System.err.println("Admin login error: " + e.getMessage());
        }
        return null;
    }

    public boolean isUsernameTaken(String username, String role) {
        String table = role.equals("admin") ? "admins" : "customers";
        String sql = "SELECT id FROM " + table + " WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Username check error: " + e.getMessage());
        }
        return false;
    }

    public boolean isCnicTaken(String cnic, String role) {
        String table = role.equals("admin") ? "admins" : "customers";
        String sql = "SELECT id FROM " + table + " WHERE cnic = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cnic);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("CNIC check error: " + e.getMessage());
        }
        return false;
    }
}