package com.close_contact_monitoring.dao;

import java.sql.*;
import java.time.LocalDate;

import com.close_contact_monitoring.model.CloseContact;

public class CloseContactDAO {
    private Connection connection;

    public CloseContactDAO(Connection connection) {
        this.connection = connection;
    }

    // Method to get the close contact case ID if a matching case is found
    public Integer getCloseContactCaseID(String lastname, String firstname, String middlename, LocalDate birthdate, char sex) {
        String sql = "SELECT closecontactID FROM closecontact WHERE lastname = ? AND firstname = ? AND middlename = ? AND birthday = ? AND sex = ? AND is_elevated_to_patient = 'Y'";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, lastname);
            stmt.setString(2, firstname);
            stmt.setString(3, middlename);
            stmt.setString(4, birthdate.toString());
            stmt.setString(5, String.valueOf(sex));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("closeContactCaseID");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking for existing close contact case: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // // Method to view close contact details by closeContactID
    // public void viewCloseContactByID(int closeContactID) {
    //     String sql = "SELECT * FROM closecontact WHERE closeContactID = ?";
    //     try (PreparedStatement stmt = connection.prepareStatement(sql)) {
    //         stmt.setInt(1, closeContactID);
    //         try (ResultSet rs = stmt.executeQuery()) {
    //             if (rs.next()) {
    //                 CloseContact closeContact = new CloseContact();
    //                 closeContact.setCloseContactID(rs.getInt("closeContactID"));
    //                 closeContact.setLastname(rs.getString("lastname"));
    //                 closeContact.setFirstname(rs.getString("firstname"));
    //                 closeContact.setMiddlename(rs.getString("middlename"));
    //                 closeContact.setBirthdate(LocalDate.parse(rs.getString("birthday")));
    //                 closeContact.setSex(rs.getString("sex").charAt(0));
    //                 closeContact.setIs_elevated_to_patient(rs.getString("is_elevated_to_patient").charAt(0));

    //                 // Print close contact details
    //                 printCloseContactDetails(closeContact);
    //             } else {
    //                 System.out.println("Close contact not found.");
    //             }
    //         }
    //     } catch (SQLException e) {
    //         System.err.println("Error retrieving close contact: " + e.getMessage());
    //         e.printStackTrace();
    //     }
    // }

    // // Method to print close contact details
    // private void printCloseContactDetails(CloseContact closeContact) {
    //     System.out.println("Close Contact ID: " + closeContact.getCloseContactID());
    //     System.out.println("Last Name: " + closeContact.getLastname());
    //     System.out.println("First Name: " + closeContact.getFirstname());
    //     System.out.println("Middle Name: " + closeContact.getMiddlename());
    //     System.out.println("Birthdate: " + closeContact.getBirthdate());
    //     System.out.println("Sex: " + closeContact.getSex());
    //     System.out.println("Is Elevated to Patient: " + closeContact.getIs_elevated_to_patient());
    // }
    
}
