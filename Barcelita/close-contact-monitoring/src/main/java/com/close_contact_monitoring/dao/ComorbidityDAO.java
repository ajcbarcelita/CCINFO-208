package com.close_contact_monitoring.dao;

import java.sql.*;

import com.close_contact_monitoring.model.Comorbidity;
import com.close_contact_monitoring.model.Patient;
import com.close_contact_monitoring.utility.InputUtility;

public class ComorbidityDAO {
    private Connection connection;
    public static final String RESET = "\033[0m";      // Reset color
    public static final String GREEN = "\033[0;32m";  // Green
    public static final String RED = "\033[0;31m";    // Red
	public static final String ORANGE = "\033[38;5;214m"; // Orange 

    public ComorbidityDAO(Connection connection) {
        this.connection = connection;
    }

    public void addComorbidityToRefTable() {
        listAllComorbidities();
        System.out.println(ORANGE + "ADD COMORBIDITY TO REF_COMORBIDITY OPERATION\n" + RESET);
        String comorbidity = InputUtility.getStringInput("Enter comorbidity name: ");
        String sql = "INSERT INTO ref_comorbidity (comorbidity) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, comorbidity);
            stmt.executeUpdate();
            System.out.println(GREEN + "Comorbidity added to ref_comorbidity table successfully." + RESET);

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int comorbidityID = generatedKeys.getInt(1);
                    System.out.println(GREEN + "Generated comorbidityID: " + comorbidityID + RESET);
                } else {
                    System.err.println(RED + "Error retrieving comorbidityID." + RESET);
                } 
            }
        } catch (SQLException e) {
            System.err.println(RED + "Failed to add comorbidity to ref_comorbidity table: " + e.getMessage() + RESET);
        }
    }

    public void addComorbidityToPatientRecord() {
        int patientID = InputUtility.getIntInput("Enter patient ID: ");
        PatientDAO patientDAO = new PatientDAO(connection);
        Patient patient = patientDAO.getPatientByID(patientID);
        if (patient == null) {
            System.err.println(RED + "Patient not found.\n" + RESET);
            return;
        } else {
            System.out.println(GREEN + "Patient found.\n" + RESET);
        }

        listAllComorbidities();
        int comorbidityID = InputUtility.getIntInput("Enter comorbidity ID: ");
        Comorbidity comorbidity = getComorbidityByID(comorbidityID);
        if (comorbidity == null) {
            System.err.println(RED + "Comorbidity not found." + RESET);
            return;
        } else {
            System.out.println(GREEN + "Comorbidity found." + RESET);
        }

        String sql = "INSERT INTO patient_comorbidity (patientID, comorbidityID) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, patientID);
            stmt.setInt(2, comorbidityID);
            stmt.executeUpdate();
            System.out.println(GREEN + "Comorbidity added to patient record successfully." + RESET);
        } catch (SQLException e) {
            System.err.println(RED + "Failed to add comorbidity to patient record: " + e.getMessage() + RESET);
        }
    }

    public void viewComorbiditiesByPatientID(int patientID) {
        String sql = "SELECT CONCAT(p.lastname, ', ', p.firstname, ' ', p.middlename) AS patientFullName, GROUP_CONCAT(rc.comorbidity ORDER BY rc.comorbidity SEPARATOR ', ') AS patient_comorbidities FROM patient_comorbidity pc JOIN ref_comorbidity rc ON pc.comorbidityID = rc.comorbidityID JOIN patient p ON p.patientID = pc.patientID WHERE p.patientID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, patientID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String patientFullName = rs.getString("patientFullName");
                    String patientComorbidities = rs.getString("patient_comorbidities");
                    System.out.println("Patient: " + patientFullName);
                    System.out.println("Comorbidities: " + patientComorbidities);
                } else {
                    System.out.println("No comorbidities found for the given patient ID.");
                }
            }
        } catch (SQLException e) {
            System.err.println(RED + "Error retrieving comorbidities for patient: " + e.getMessage() + RESET);
            e.printStackTrace();
        }
    }

    public Comorbidity getComorbidityByID(int comorbidityID) {
        String sql = "SELECT * FROM ref_comorbidity WHERE comorbidityID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, comorbidityID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Comorbidity comorbidity = new Comorbidity();
                    comorbidity.setComorbidityID(rs.getInt("comorbidityID"));
                    comorbidity.setComorbidityName(rs.getString("comorbidity"));
                    return comorbidity;
                }
            }
        } catch (SQLException e) {
            System.err.println(RED + "Error retrieving comorbidity: " + e.getMessage() + RESET);
            e.printStackTrace();
        }
        return null;
    }

    public void listAllComorbidities() {
        String sql = "SELECT * FROM ref_comorbidity";
        try (Statement stmt = connection.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    System.out.println("Comorbidity ID: " + rs.getInt("comorbidityID"));
                    System.out.println("Comorbidity Name: " + rs.getString("comorbidity"));
                    System.out.println();
                }
                System.out.println();
            }
        } catch (SQLException e) {
            System.err.println(RED + "Error retrieving comorbidities: " + e.getMessage() + RESET);
            e.printStackTrace();
        }
    }
}