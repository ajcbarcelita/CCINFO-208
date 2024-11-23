package com.close_contact_monitoring.dao;

import com.close_contact_monitoring.model.*;
import java.sql.*;
import java.time.LocalDate;

import com.close_contact_monitoring.utility.InputUtility;

public class GuardianDAO {
    private Connection connection;
    public static final String RESET = "\033[0m";      // Reset color
    public static final String GREEN = "\033[0;32m";  // Green
    public static final String RED = "\033[0;31m";    // Red
	public static final String ORANGE = "\033[38;5;214m"; // Orange 

    public GuardianDAO(Connection connection) {
        this.connection = connection;
    }

    //add guardian to patient record
    public void addGuardianToPatientRecord() {
        int patientID = InputUtility.getIntInput("Enter Patient ID: ");
        PatientDAO patientDAO = new PatientDAO(connection);
        Patient patient = patientDAO.getPatientByID(patientID);
        if (patient == null) {
            System.err.println(RED + "Patient not found.\n" + RESET);
            return;
        } else {
            System.out.println(GREEN + "Patient found.\n" + RESET);
        }
        
        // Gather guardian details from the user
        String guardianLastname = InputUtility.getStringInput("Enter Guardian Last Name*: ");
        String guardianFirstname = InputUtility.getStringInput("Enter Guardian First Name*: ");
        String guardianMiddlename = InputUtility.getStringInput("Enter Guardian Middle Name*: ");
        LocalDate guardianBirthdate = InputUtility.getDateInput("Enter Guardian Birthdate (YYYY-MM-DD)*: ");
        char guardianSex = InputUtility.getCharInput("Enter guardian's sex (M/F)*: ");
        String guardianContactNumber = InputUtility.getStringInput("Enter Guardian Contact Number*: ");
        String guardianEmail = InputUtility.getStringInput("Enter Guardian Email: ");
        String relationship = InputUtility.getStringInput("Enter Relationship to Patient (Father, Mother, Legal Guardian, Relative, or Other)*: ");
        char isEmergencyContact = InputUtility.getCharInput("Is Emergency Contact (Y/N)*: ");

        // Insert the guardian into the guardian table
        String insertGuardianSQL = "INSERT INTO guardian (lastname, firstname, middlename, birthdate, sex, contactno, email, relationship_to_patient, is_emergency_contact) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(insertGuardianSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, guardianLastname);
            stmt.setString(2, guardianFirstname);
            stmt.setString(3, guardianMiddlename);
            stmt.setDate(4, Date.valueOf(guardianBirthdate));
            stmt.setString(5, String.valueOf(guardianSex));
            stmt.setString(6, guardianContactNumber);
            stmt.setString(7, guardianEmail);
            stmt.setString(8, relationship);
            stmt.setString(9, String.valueOf(isEmergencyContact));
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int guardianID = generatedKeys.getInt(1);
                        System.out.println(GREEN + "Guardian added successfully with ID: " + guardianID + RESET);

                        // Associate the guardian with the patient
                        String associateGuardianSQL = "INSERT INTO patient_guardian (patientID, guardianID) VALUES (?, ?)";
                        try (PreparedStatement associateStmt = connection.prepareStatement(associateGuardianSQL)) {
                            associateStmt.setInt(1, patientID);
                            associateStmt.setInt(2, guardianID);
                            int associationRowsInserted = associateStmt.executeUpdate();
                            if (associationRowsInserted > 0) {
                                System.out.println(GREEN + "Guardian associated with patient successfully." + RESET);
                            } else {
                                System.err.println(RED + "Error: Failed to associate guardian with patient." + RESET);
                            }
                        }
                    } else {
                        System.err.println(RED + "Error: Failed to retrieve generated guardian ID." + RESET);
                    }
                }
            } else {
                System.err.println(RED + "Error: Failed to add guardian." + RESET);
            }
        } catch (SQLException e) {
                System.err.println(RED + "Error adding guardian: " + e.getMessage() + RESET);
                e.printStackTrace();
         }
    }


    public void viewGuardiansOfPatient(int patientID) {
        String sql = "SELECT p.patientID, CONCAT(p.lastname, ', ', p.firstname, ' ', p.middlename) AS patientFullName, " +
                     "g.guardianID, CONCAT(g.lastname, ', ', g.firstname, ' ', g.middlename) AS guardianFullName, " +
                     "g.relationship_to_patient, g.is_emergency_contact FROM patient_guardian pg " +
                     "JOIN guardian g ON g.guardianID = pg.guardianID " +
                     "JOIN patient p ON p.patientID = pg.patientID " +
                     "WHERE pg.patientID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, patientID);
            try (ResultSet rs = stmt.executeQuery()) {
                boolean patientInfoPrinted = false;
                while (rs.next()) {
                    if (!patientInfoPrinted) {
                        String patientFullName = rs.getString("patientFullName");
                        System.out.println(ORANGE + "Guardians for Patient ID " + patientID + ":" + RESET);
                        System.out.println("Patient ID: " + patientID);
                        System.out.println("Patient Full Name: " + patientFullName + "\n");
                        patientInfoPrinted = true;
                    }
                    int guardianID = rs.getInt("guardianID");
                    String guardianFullName = rs.getString("guardianFullName");
                    String relationship = rs.getString("relationship_to_patient");
                    boolean isEmergencyContact = rs.getBoolean("is_emergency_contact");

                    System.out.println("Guardian ID: " + guardianID);
                    System.out.println("Guardian Full Name: " + guardianFullName);
                    System.out.println("Relationship to Patient: " + relationship);
                    System.out.println("Is Emergency Contact: " + (isEmergencyContact ? "Yes" : "No"));
                    System.out.println();
                }
            }
        } catch (SQLException e) {
            System.err.println(RED + "Error retrieving guardians for patient: " + e.getMessage() + RESET);
            e.printStackTrace();
        }
    }       
}
