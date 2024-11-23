package com.ccinfo208_dbapp.Barcelita.dao;

import com.ccinfo208_dbapp.Barcelita.model.Patient;
import com.ccinfo208_dbapp.Barcelita.model.Address;
import com.ccinfo208_dbapp.Barcelita.utility.InputUtility;

import java.time.LocalDate;
import java.sql.*;
import java.util.ArrayList;


public class PatientDAO {
    public static final String RESET = "\033[0m";      // Reset color
    public static final String GREEN = "\033[0;32m";  // Green
    public static final String RED = "\033[0;31m";    // Red
	public static final String ORANGE = "\033[38;5;214m"; // Orange 
    private Connection connection;

    public PatientDAO(Connection connection) {
        this.connection = connection;
    }

    public void addPatient() {
        //First gather input from the user for most of patient info
        System.out.println(ORANGE + "ADD PATIENT OPERATION\n" + RESET);
        String lastname = InputUtility.getStringInput("Enter lastname*: ");
        String firstname = InputUtility.getStringInput("Enter firstname*: ");
        String middlename = InputUtility.getStringInput("Enter middlename*: ");
        LocalDate birthdate = InputUtility.getDateInput("Enter birthdate (YYYY-MM-DD)*: ");
        char sex = InputUtility.getCharInput("Enter sex (M/F)*: ");
        char is_bcg_vaccinated = InputUtility.getCharInput("Is the patient BCG vaccinated? (Y/N)*: ");

        //AddressID is a foreign key to the Address table, so prompt for creation of Address first
        AddressDAO addressDAO = new AddressDAO(connection);
        Integer addressID = addressDAO.addAddress();
        if (addressID == null) {
            System.err.println("Error: Address creation failed. Patient not added.");
            return;
        }

        //Close contact case ID search is automatic, no need for user input, can be null
        CloseContactDAO closeContactDAO = new CloseContactDAO(connection);
        Integer closeContactCaseID = closeContactDAO.getCloseContactCaseID(lastname, firstname, middlename, birthdate, sex);

        //Start insert operation
        String sql = "INSERT INTO patient (lastname, firstname, middlename, birthday, sex, is_bcg_vaccinated, addressID, closeContactCaseID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, lastname);
            stmt.setString(2, firstname);
            stmt.setString(3, middlename);
            stmt.setString(4, birthdate.toString());
            stmt.setString(5, String.valueOf(sex));
            stmt.setString(6, String.valueOf(is_bcg_vaccinated));
            stmt.setInt(7, addressID);
            if (closeContactCaseID == null) {
                stmt.setNull(8, Types.INTEGER);
            } else {
                stmt.setInt(8, closeContactCaseID);
            }
            stmt.executeUpdate();
            // Retrieve the generated patientID

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int patientID = generatedKeys.getInt(1);
                    System.out.println("Patient added successfully with ID: " + patientID);
                } else {
                    System.err.println("Error: Failed to retrieve generated patient ID.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding patient: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public Patient getPatientByID(int patientID) {
        String sql = "SELECT * FROM patient WHERE patientID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, patientID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Patient patient = new Patient();
                    patient.setPatientID(rs.getInt("patientID"));
                    patient.setLastname(rs.getString("lastname"));
                    patient.setFirstname(rs.getString("firstname"));
                    patient.setMiddlename(rs.getString("middlename"));
                    patient.setBirthdate(LocalDate.parse(rs.getString("birthday")));
                    patient.setSex(rs.getString("sex").charAt(0));
                    patient.setIs_bcg_vaccinated(rs.getString("is_bcg_vaccinated").charAt(0));
                    patient.setAddressID(rs.getInt("addressID"));
                    patient.setClosecontactcaseID(rs.getInt("closecontactcaseID"));
                    return patient;
                } else {
                    System.out.println("Patient not found.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving patient: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public void updatePatient(int patientID) {
        // Retrieve the existing patient details
        Patient existingPatient = getPatientByID(patientID);
        if (existingPatient == null) {
            System.out.println("Patient not found.");
            return;
        }
        printPatientDetails(existingPatient);
        // Gather new input from the user
        String newLastname = InputUtility.getStringInput("Enter new lastname (current: " + existingPatient.getLastname() + "): ");
        String newFirstname = InputUtility.getStringInput("Enter new firstname (current: " + existingPatient.getFirstname() + "): ");
        String newMiddlename = InputUtility.getStringInput("Enter new middlename (current: " + existingPatient.getMiddlename() + "): ");
        LocalDate newBirthdate = InputUtility.getDateInput("Enter new birthdate (YYYY-MM-DD) (current: " + existingPatient.getBirthdate() + "): ");
        char newSex = InputUtility.getCharInput("Enter new sex (M/F) (current: " + existingPatient.getSex() + "): ");
        char newIsBcgVaccinated = InputUtility.getCharInput("Is the patient BCG vaccinated? (Y/N) (current: " + existingPatient.getIs_bcg_vaccinated() + "): ");

        AddressDAO addressDAO = new AddressDAO(connection);
        addressDAO.updateAddress(existingPatient.getAddressID());
    
        // Update the patient record in the database
        String sql = "UPDATE patient SET lastname = ?, firstname = ?, middlename = ?, birthday = ?, sex = ?, is_bcg_vaccinated = ? WHERE patientID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newLastname);
            stmt.setString(2, newFirstname);
            stmt.setString(3, newMiddlename);
            stmt.setString(4, newBirthdate.toString());
            stmt.setString(5, String.valueOf(newSex));
            stmt.setString(6, String.valueOf(newIsBcgVaccinated));
            stmt.setInt(7, patientID);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Patient updated successfully.");
            } else {
                System.out.println("Error: Patient update failed.");
            }
        } catch (SQLException e) {
            System.err.println("Error updating patient: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // public void softDeletePatient(int patientID) {

    // }

    public ArrayList<Patient> getPatientsByBCGVaccinationStatus(char isBCGVaccinated) {
        ArrayList<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patient WHERE is_bcg_vaccinated = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, String.valueOf(isBCGVaccinated));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Patient patient = new Patient();
                    patient.setPatientID(rs.getInt("patientID"));
                    patient.setLastname(rs.getString("lastname"));
                    patient.setFirstname(rs.getString("firstname"));
                    patient.setMiddlename(rs.getString("middlename"));
                    patient.setBirthdate(LocalDate.parse(rs.getString("birthday")));
                    patient.setSex(rs.getString("sex").charAt(0));
                    patient.setAddressID(rs.getInt("addressID"));
                    patient.setIs_bcg_vaccinated(rs.getString("is_bcg_vaccinated").charAt(0));
                    patients.add(patient);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving patients by BCG vaccination status: " + e.getMessage());
            e.printStackTrace();
        }
        return patients;
    }
    public ArrayList<Patient> getPatientsByCloseContactStatus(boolean hasCloseContactID) {
        ArrayList<Patient> patients = new ArrayList<>();
        String sql = hasCloseContactID ? 
            "SELECT * FROM patient WHERE closecontactcaseID IS NOT NULL" : 
            "SELECT * FROM patient WHERE closecontactcaseID IS NULL";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Patient patient = new Patient();
                    patient.setPatientID(rs.getInt("patientID"));
                    patient.setLastname(rs.getString("lastname"));
                    patient.setFirstname(rs.getString("firstname"));
                    patient.setMiddlename(rs.getString("middlename"));
                    patient.setBirthdate(LocalDate.parse(rs.getString("birthday")));
                    patient.setSex(rs.getString("sex").charAt(0));
                    patient.setIs_bcg_vaccinated(rs.getString("is_bcg_vaccinated").charAt(0));
                    patient.setAddressID(rs.getInt("addressID"));
                    patient.setClosecontactcaseID(rs.getInt("closecontactcaseID"));
                    patients.add(patient);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving patients by close contact status: " + e.getMessage());
            e.printStackTrace();
        }
        return patients;
    }

    public void getPatientAndRelatedRecords(int patientID) {
        // First part for everything except patient's guardians
        String sqlPatientCoreData = "SELECT * FROM patientcoredata_view WHERE patientID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sqlPatientCoreData)) {
            stmt.setInt(1, patientID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println();
                    System.out.println("Patient Core Data:");
                    System.out.println("Patient ID: " + rs.getInt("patientID"));
                    System.out.println("Full Name: " + rs.getString("patientfullname"));
                    System.out.println("Birthdate: " + rs.getString("birthday"));
                    System.out.println("Sex: " + rs.getString("sex"));
                    System.out.println("BCG Vaccinated: " + rs.getString("is_bcg_vaccinated"));
                    System.out.println("Comorbidities: " + rs.getString("patient_comorbidities"));
                    System.out.println("Address: " + rs.getString("patientaddress"));
                    System.out.println("Close Contact ID: " + rs.getInt("closecontactID"));
                    System.out.println("Is Elevated to Patient: " + rs.getString("is_elevated_to_patient"));
                    // Add more fields as needed
                } else {
                    System.out.println("No patient core data found for the given patient ID.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving patient core data: " + e.getMessage());
            e.printStackTrace();
        }

        // Second part for patient's guardians
        String sqlPatientGuardians = "SELECT * FROM patientguardians_view WHERE patientID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sqlPatientGuardians)) {
            stmt.setInt(1, patientID);
            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println();
                System.out.println("Patient Guardians:");
                while (rs.next()) {
                    System.out.println("Guardian ID: " + rs.getInt("guardianID"));
                    System.out.println("Full Name: " + rs.getString("guardian_fullname"));
                    System.out.println("Birthdate: " + rs.getString("birthdate"));
                    System.out.println("Sex: " + rs.getString("sex"));
                    System.out.println("Contact Number: " + rs.getString("contactno"));
                    System.out.println("Email: " + rs.getString("email"));
                    System.out.println("Relationship: " + rs.getString("relationship_to_patient"));
                    System.out.println("Is Emergency Contact: " + rs.getString("is_emergency_contact"));
                    // Add more fields as needed
                    System.out.println();
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving patient guardians: " + e.getMessage());
            e.printStackTrace();
        }
    }

    
    public void printPatientDetails(Patient patient) {
        System.out.println("Patient ID: " + patient.getPatientID());
        System.out.println("Full Name: " + patient.getLastname() + ", " + patient.getFirstname() + " " + patient.getMiddlename());
        System.out.println("Birthdate: " + patient.getBirthdate());
        System.out.println("Sex: " + patient.getSex());
        System.out.println("BCG Vaccinated: " + patient.getIs_bcg_vaccinated());
        AddressDAO addressDAO = new AddressDAO(connection);
        Address address = addressDAO.getAddressByID(patient.getAddressID());
        System.out.println("Address: " + address.getCa_houseno() + " " + address.getCa_streetname() + " " + address.getCa_subdivision());
        //print pit barangay name
        BarangayDAO barangayDAO = new BarangayDAO(connection);
        barangayDAO.viewBarangayByID(address.getCa_barangay());
        barangayDAO.printBarangayDetails(barangayDAO.viewBarangayByID(address.getCa_barangay()));
        Integer closeContactCaseID = patient.getClosecontactcaseID();
        System.out.println("Close Contact Case ID: " + (closeContactCaseID != 0 ? closeContactCaseID : "Null"));
        System.out.println();
    }
}
