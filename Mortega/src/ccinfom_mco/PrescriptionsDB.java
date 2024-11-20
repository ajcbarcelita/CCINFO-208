package ccinfom_mco;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;

public class PrescriptionsDB {

    // variables
    private static final String DB_URL = "";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "DLSU1234!";

    public String prescriptionSerialNumber;
    public int patientCaseID;
    public int physicianPRCLicNumber;
    public Date date;                          
    public ArrayList<String> prescriptionNames = new ArrayList<>();
    public ArrayList<Double> dosage = new ArrayList<>();
    public ArrayList<Integer> frequency = new ArrayList<>();
    public ArrayList<Integer> duration = new ArrayList<>();


    //constructor
    public PrescriptionsDB(){
        this.prescriptionSerialNumber = "";
        this.patientCaseID = 0; //not auto incremented. inputted
        this.physicianPRCLicNumber = 0;
        this.date = null; 
    }

    public static int checkPhyID(int physicianId) {
        String sql = "SELECT 1 FROM physician WHERE physician_prc_id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, physicianId);
            ResultSet rs = pstmt.executeQuery();

            // If a result is found, it means the physician ID exists
            if (rs.next()) {
                return 1; // Physician ID exists
            } else {
                return 0; // Physician ID does not exist
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return 0; 
        }
    } //is ths method supposed to know if the prc is assigned to the patient case 

    
    
    // Method to check if the physician is involved in the case
    public static int checkPhyID(int physicianId, int caseno) {
        String sql = "SELECT involved_physician FROM patient_case WHERE caseno = ?";
    
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
            pstmt.setInt(1, caseno);
            ResultSet rs = pstmt.executeQuery();
     
            // If a result is found, it means the case number exists
            if (rs.next()) {
                int involvedPhysician = rs.getInt("involved_physician");
    
                // Check if the given physician ID matches the involved physician for this case
                if (involvedPhysician == physicianId) {
                    return 1; // Physician is involved in the case
                } else {
                    return 0; // Physician is not involved in the case
                }
    
            } else {
                return 0; // Case number does not exist
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
            return 0; // Return 0 in case of an error
        }
    }
    



    //checks if the patient_case ID exists
    //returns 1, if it exists. 0, otherwise
    public static int checkCaseNo(int caseno) {
        String sql = "SELECT 1 FROM patient_case WHERE caseno = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
            pstmt.setInt(1, caseno);
            ResultSet rs = pstmt.executeQuery();
    
            if (rs.next()) {
                return 1; 
            } else {
                return 0; 
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
            return 0; 
        }      
    }           


    /*
     * Method to check the value of toBePrescribed field for a given case number
     * returns 1, if toBePrescribed field in the patient_case table
     * is set to true
     * 
     * returns 0, otherwise
     */
    public static int toBePrescribed(int caseno) {
        String sql = "SELECT toBePrescribed FROM patient_case WHERE caseno = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, caseno);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String toBePrescribedValue = rs.getString("toBePrescribed");
                if ("Y".equalsIgnoreCase(toBePrescribedValue)) {
                    return 1; // toBePrescribed is 'Y'
                } else if ("N".equalsIgnoreCase(toBePrescribedValue)) {
                    return 0; // toBePrescribed is 'N'
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0; // Default return value if no record is found or if the value is not 'Y' or 'N'
    }



    // Method to check if a prescription exists using the prescription serial number
    public static int checkPresNo(String presSerialNumber) {
        String sql = "SELECT 1 FROM prescription WHERE prescription_serialno = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, presSerialNumber);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return 1; // Prescription serial number exists
            } else {
                return 0; // Prescription serial number does not exist
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return 0; 
        }
    }



    /*
     * Method to check if a medicine exists in the ref_medicine table
     *  return 1 if medicine exists, 0 otherwise.
     */
    public static int checkMedicine(String genericName) {
        String sql = "SELECT 1 FROM ref_medicine WHERE generic_name = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, genericName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return 1; // Medicine exists
            } else {
                return 0; // Medicine does not exist
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return 0; 
        }
    }



    //returns 1 if successful, 0 otherwise
//check if pres serial number doesnt exist yet
public int submit_prescriptions() {
    Connection conn = null;
    PreparedStatement insertPrescription = null;
    PreparedStatement insertMedicine = null;

    try {
        conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        conn.setAutoCommit(false);

        // insert prescription into prescription table
        String insertPrescriptionSQL = "INSERT INTO prescription (prescription_serialno, caseno, physician_prcID, date_given) VALUES (?, ?, ?, ?)";
        insertPrescription = conn.prepareStatement(insertPrescriptionSQL);
        insertPrescription.setString(1, prescriptionSerialNumber);
        insertPrescription.setInt(2, patientCaseID);
        insertPrescription.setInt(3, physicianPRCLicNumber);
        insertPrescription.setDate(4, date);

        insertPrescription.executeUpdate();

        // insert medicines into prescription_medicine table
        String insertMedicineSQL = "INSERT INTO prescription_medicine (prescription_serialno, generic_name, dosage, dose_frequency, duration) VALUES (?, ?, ?, ?, ?)";
        insertMedicine = conn.prepareStatement(insertMedicineSQL);

        for (int i = 0; i < prescriptionNames.size(); i++) {
            insertMedicine.setString(1, prescriptionSerialNumber); //FK to prescription
            insertMedicine.setString(2, prescriptionNames.get(i));
            insertMedicine.setDouble(3, dosage.get(i)); // Changed to setDouble
            insertMedicine.setInt(4, frequency.get(i));
            insertMedicine.setInt(5, duration.get(i));

            insertMedicine.addBatch(); // group commands
        }

        // execute batch insert for prescription_medicine in one go
        insertMedicine.executeBatch();

        // save
        conn.commit();
        System.out.println("Prescription and medicines successfully submitted.");

        return 1; // saved

    } catch (SQLException e) {
        if (conn != null) {
            try {
                conn.rollback(); //  undo the changes made in the transaction
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        }
        e.printStackTrace();
        return 0; // failed
    } finally {
        try {
            if (insertMedicine != null) insertMedicine.close();
            if (insertPrescription != null) insertPrescription.close();
            if (conn != null) conn.close();
        } catch (SQLException closeEx) {
            closeEx.printStackTrace();
        }
    }
}

    
                                                  
    /* NOTE: 
       get the size of the medicine_name arraylist
       use that to determine how many copies of prescription_medicide
       will be created for the prescription

       ex:
       int numberOfMed = 6;
       
       then create 6 rows of prescription_medicine
       having the same prescription serial number
       but different generic_name
     */


     public int view_prescriptions() {
        String prescriptionQuery = "SELECT * FROM prescription WHERE caseno = ? AND prescription_serialno = ?";
        String medicineQuery = "SELECT * FROM prescription_medicine WHERE prescription_serialno = ?";
        
        Connection conn = null;
        PreparedStatement prescriptionStmt = null;
        PreparedStatement medicineStmt = null;
        ResultSet prescriptionRs = null;
        ResultSet medicineRs = null;
    
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    
            // Prescription details
            prescriptionStmt = conn.prepareStatement(prescriptionQuery);
            prescriptionStmt.setInt(1, patientCaseID);
            prescriptionStmt.setString(2, prescriptionSerialNumber);
            prescriptionRs = prescriptionStmt.executeQuery();
    
            // No prescription, return 0
            if (!prescriptionRs.next()) {
                System.out.println("No prescription found for the given case number and serial number.");
                return 0;
            }
    
            System.out.println("=======================================================");
            System.out.println("                 Prescription Details ");
            System.out.println("=======================================================");
    
            System.out.println("Prescription Serial Number: " + prescriptionRs.getString("prescription_serialno"));
            System.out.println("Patient Case Number: " + prescriptionRs.getInt("caseno"));
            System.out.println("Physician PRC License Number: " + prescriptionRs.getInt("physician_prcID"));
            System.out.println("Date: " + prescriptionRs.getDate("date_given"));
    
            medicineStmt = conn.prepareStatement(medicineQuery);
            medicineStmt.setString(1, prescriptionSerialNumber);
            medicineRs = medicineStmt.executeQuery();
    
            // Display medicines
            System.out.println("\nMedicines:");
            while (medicineRs.next()) {
                System.out.println("Medicine Name: " + medicineRs.getString("generic_name"));
                System.out.println("Dosage: " + medicineRs.getDouble("dosage")); // Changed to getDouble
                System.out.println("Dose Frequency: " + medicineRs.getString("dose_frequency"));
                System.out.println("Duration: " + medicineRs.getString("duration"));
                System.out.println("-------------------------------------------------------");
            }
    
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Error
        } finally {
            try {
                if (prescriptionRs != null) prescriptionRs.close();
                if (medicineRs != null) medicineRs.close();
                if (prescriptionStmt != null) prescriptionStmt.close();
                if (medicineStmt != null) medicineStmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    


    public int delete_prescriptions() {
        String deleteMedicineQuery = "DELETE FROM prescription_medicine WHERE prescription_serialno = ?";
        String deletePrescriptionQuery = "DELETE FROM prescription WHERE caseno = ? AND prescription_serialno = ?";
        
        Connection conn = null;
        PreparedStatement deleteMedicineStmt = null;
        PreparedStatement deletePrescriptionStmt = null;
    
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    
            conn.setAutoCommit(false);
    
            // delete medicines from prescription_medicine 
            deleteMedicineStmt = conn.prepareStatement(deleteMedicineQuery);
            deleteMedicineStmt.setString(1, prescriptionSerialNumber);
            deleteMedicineStmt.executeUpdate();
    
            // delete prescription from the prescription 
            deletePrescriptionStmt = conn.prepareStatement(deletePrescriptionQuery);
            deletePrescriptionStmt.setInt(1, patientCaseID);
            deletePrescriptionStmt.setString(2, prescriptionSerialNumber);
            int prescriptionRowsDeleted = deletePrescriptionStmt.executeUpdate();
    
            if (prescriptionRowsDeleted == 0) {
                System.out.println("No matching prescription found for deletion.");
                conn.rollback(); 
                return 0;
            }
    
            conn.commit();
            System.out.println("Prescription and associated medicines deleted successfully.");
            return 1; 

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback(); 
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            return -1; 
        } finally {
            try {
                if (deleteMedicineStmt != null) deleteMedicineStmt.close();
                if (deletePrescriptionStmt != null) deletePrescriptionStmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    

}

