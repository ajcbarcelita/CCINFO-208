package com.close_contact_monitoring.dao;

import java.sql.*;

import com.close_contact_monitoring.model.Barangay;

public class BarangayDAO {
    private Connection connection;

    public BarangayDAO(Connection connection) {
        this.connection = connection;
    }

    public Barangay viewBarangayByID(int barangayID) {
        String sql = "SELECT * FROM barangay WHERE barangayID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, barangayID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Barangay barangay = new Barangay();
                    barangay.setBarangayID(rs.getInt("barangayID"));
                    barangay.setBarangayName(rs.getString("barangayName"));
                    return barangay;
                } else {
                    System.out.println("Barangay not found.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving barangay: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Method to print barangay details
    public void printBarangayDetails(Barangay barangay) {
        System.out.println("Barangay Name: " + barangay.getBarangayName());
    }



}
