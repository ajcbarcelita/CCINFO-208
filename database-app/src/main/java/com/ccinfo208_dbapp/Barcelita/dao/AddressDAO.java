package com.ccinfo208_dbapp.Barcelita.dao;

import com.ccinfo208_dbapp.Barcelita.model.Address;
import com.ccinfo208_dbapp.Barcelita.utility.InputUtility;

import java.sql.*;

public class AddressDAO {
    public static final String RESET = "\033[0m";      // Reset color
    public static final String GREEN = "\033[0;32m";  // Green
    public static final String RED = "\033[0;31m";    // Red
	public static final String ORANGE = "\033[38;5;214m"; // Orange 
    private Connection connection;

    public AddressDAO(Connection connection) {
        this.connection = connection;
    }

    // Method to check if a barangay exists in the Barangay table by name
    public Integer getBarangayIDByName(String barangayName) throws SQLException {
        String sql = "SELECT barangayID FROM barangay WHERE barangayName = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, barangayName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("barangayID");
                }
            }
        }
        return null;
    }

    // Method to add an address to the Address table and return the generated addressID
    public Integer addAddress() {
        // Gather input from the user
        String ca_houseno = InputUtility.getStringInput("Enter house number*: ");
        String ca_subdivision = InputUtility.getStringInput("Enter subdivision: ");
        String ca_streetname = InputUtility.getStringInput("Enter street name*: ");
        String barangayName = InputUtility.getStringInput("Enter barangay name*: ");

        try {
            // Check if the barangay exists
            Integer barangayID = getBarangayIDByName(barangayName);
            if (barangayID == null) {
                System.err.println("Error: Barangay with name " + barangayName + " does not exist in Barangay Table.");
                return null;
            }

            // Perform the database operation
            String sql = "INSERT INTO currentaddress (ca_houseno, ca_subdivision, ca_streetname, ca_barangay) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, ca_houseno);
                stmt.setString(2, ca_subdivision);
                stmt.setString(3, ca_streetname);
                stmt.setInt(4, barangayID);
                stmt.executeUpdate();

                // Retrieve the generated addressID
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int addressID = generatedKeys.getInt(1);
                        System.out.println("Address added successfully with ID: " + addressID);
                        return addressID;
                    } else {
                        System.err.println("Error: Failed to retrieve generated address ID.");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding address: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public Address getAddressByID(int addressID) {
        String sql = "SELECT * FROM currentaddress WHERE addressID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, addressID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Address address = new Address();
                    address.setAddressID(rs.getInt("addressID"));
                    address.setCa_houseno(rs.getString("ca_houseno"));
                    address.setCa_subdivision(rs.getString("ca_subdivision"));
                    address.setCa_streetname(rs.getString("ca_streetname"));
                    address.setCa_barangay(rs.getInt("ca_barangay"));
                    return address;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving address: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Method to update an address
    public void updateAddress(int addressID) {
        // View the current address details
        Address existingAddress = getAddressByID(addressID);
        if (existingAddress == null) {
            System.out.println("Address not found.");
            return;
        }
        // Gather new input from the user
        String newCa_houseno = InputUtility.getStringInput("Enter new house number: ");
        String newCa_subdivision = InputUtility.getStringInput("Enter new subdivision: ");
        String newCa_streetname = InputUtility.getStringInput("Enter new street name: ");
        String newBarangayName = InputUtility.getStringInput("Enter new barangay name: ");

        try {
            // Check if the barangay exists
            Integer newBarangayID = getBarangayIDByName(newBarangayName);
            if (newBarangayID == null) {
                System.err.println("Error: Barangay with name " + newBarangayName + " does not exist in Barangay Table.");
                return;
            }

            // Perform the database operation
            String sql = "UPDATE currentaddress SET ca_houseno = ?, ca_subdivision = ?, ca_streetname = ?, ca_barangay = ? WHERE addressID = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, newCa_houseno);
                stmt.setString(2, newCa_subdivision);
                stmt.setString(3, newCa_streetname);
                stmt.setInt(4, newBarangayID);
                stmt.setInt(5, addressID);
                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Address updated successfully.");
                } else {
                    System.out.println("Error: Address update failed.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error updating address: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void printAddress(Address address) {
        System.out.println("House Number: " + address.getCa_houseno());
        System.out.println("Subdivision: " + address.getCa_subdivision());
        System.out.println("Street Name: " + address.getCa_streetname());
        int barangayID = address.getCa_barangay();
        BarangayDAO barangayDAO = new BarangayDAO(connection);
        barangayDAO.viewBarangayByID(barangayID);
    }
}
