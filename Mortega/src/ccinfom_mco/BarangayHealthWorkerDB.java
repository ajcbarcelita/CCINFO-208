package ccinfom_mco;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class BarangayHealthWorkerDB {

    // Common Algorithm when Interacting with Databases
			// 1. Connect to the MYSQL Database
			// 2. Preparing your SQL Statement
			// 3. Execute our SQL Statement (and if there are results, store it in a variable)
			// 4. Close our SQL Statement
			// 5. Close our Connection to the MYSQL Database

    // variables
	private static final String DB_URL = "jdbc:mysql://153.92.15.3:3306/u400425564_ccinfo208db";
    private static final String DB_USER = "u400425564_root";
    private static final String DB_PASSWORD = "DLSUm1234!";

	public int bhwID;
	public String bhw_lastName;
	public String bhw_firstName;
	public String bhw_middleName;
	public String bhw_email;
	public String barangayAssignedTo;
	public String bhw_isActive;

    //constructor
	public BarangayHealthWorkerDB() {
		this.bhwID = 0;
		this.bhw_lastName = "";
		this.bhw_firstName = "";
		this.bhw_middleName = "";
		this.bhw_email = "";
		this.barangayAssignedTo = "";
		this.bhw_isActive = "";
	}
 
	public int create_bhw_record() {
        String sql = "INSERT INTO bhw (lastname, firstname, middlename, barangay_assignedto) VALUES (?, ?, ?, ?)";
    
        try (
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            System.out.println("Connection to DB Successful");
    
            // Set parameters for the prepared statement
            pstmt.setString(1, bhw_lastName);
            pstmt.setString(2, bhw_firstName);
            pstmt.setString(3, bhw_middleName);
            pstmt.setInt(4, getBrgyNo(barangayAssignedTo)); // Assumes input is validated in the controller
            
            System.out.println("SQL Statement Prepared");
    
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new BHW was inserted successfully!");
    
                // Retrieve the auto-generated BHWID
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        System.out.println("The ID assigned to the BHW is: " + generatedId);
                    }
                }
            }
            return 1;
    
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    

	public BHW view_bhw_record() {
		String sql = "SELECT bhwID, lastname, firstname, middlename, barangay_assignedto FROM bhw WHERE bhwID = ?";
        BHW bhw = null;

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, bhwID);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
					bhw = new BHW();
                    bhw.setBhwID(rs.getInt("bhwID"));
                    bhw.setLastname(rs.getString("lastname"));
                    bhw.setFirstname(rs.getString("firstname"));
                    bhw.setMiddlename(rs.getString("middlename"));
                    bhw.setBarangayAssignedTo(rs.getInt("barangay_assignedto"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bhw; // create a method printer
	}


	//checks if the bhw_id exists in the database
	//returns 1 if it exists. 0, otherwise
	public static int checkBhwId(int bhwID) {
        String sql = "SELECT bhwID FROM bhw WHERE bhwID = ?";
        int exists = 0; // Default to 0 (not found)

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, bhwID);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    exists = 1; 
                }
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return exists;
    }

	//updates existing bhw record
	//returns 1 if successful. 0, otherwise
	public int update_bhw_record() {
        // Debug: Print the current state of fields
        System.out.println("Debug: bhw_firstName = " + bhw_firstName);
        System.out.println("Debug: barangayAssignedTo = " + barangayAssignedTo);
    
        StringBuilder sql = new StringBuilder("UPDATE bhw SET ");
        boolean hasValidField = false;
    
        // Dynamically build the SQL query
        if (bhw_lastName != null && !bhw_lastName.equalsIgnoreCase("N/A")) {
            sql.append("lastname = ?");
            hasValidField = true;
        }
        if (bhw_firstName != null && !bhw_firstName.equalsIgnoreCase("N/A")) {
            if (hasValidField) sql.append(", ");
            sql.append("firstname = ?");
            hasValidField = true;
        }
        if (bhw_middleName != null && !bhw_middleName.equalsIgnoreCase("N/A")) {
            if (hasValidField) sql.append(", ");
            sql.append("middlename = ?");
            hasValidField = true;
        }
        if (barangayAssignedTo != null && !barangayAssignedTo.equalsIgnoreCase("N/A")) {
            if (hasValidField) sql.append(", ");
            sql.append("barangay_assignedto = ?");
            hasValidField = true;
        }
    
        // Add WHERE clause
        sql.append(" WHERE bhwID = ?");
    
        // If no fields to update
        if (!hasValidField) {
            System.out.println("No valid fields to update for BHW ID: " + bhwID);
            return 0;
        }
    
        try (
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(sql.toString())
        ) {
            int parameterIndex = 1;
    
            // Set dynamic parameters
            if (bhw_lastName != null && !bhw_lastName.equalsIgnoreCase("N/A")) {
                pstmt.setString(parameterIndex++, bhw_lastName);
            }
            if (bhw_firstName != null && !bhw_firstName.equalsIgnoreCase("N/A")) {
                pstmt.setString(parameterIndex++, bhw_firstName);
            }
            if (bhw_middleName != null && !bhw_middleName.equalsIgnoreCase("N/A")) {
                pstmt.setString(parameterIndex++, bhw_middleName);
            }
            if (barangayAssignedTo != null && !barangayAssignedTo.equalsIgnoreCase("N/A")) {
                int barangayID = getBrgyNo(barangayAssignedTo);
                System.out.println("Debug: Resolved barangay ID = " + barangayID); // Debug output
                pstmt.setInt(parameterIndex++, barangayID);
            }
    
            pstmt.setInt(parameterIndex, bhwID); // Set BHW ID for WHERE clause
    
            // Execute update
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("BHW record updated successfully for BHW ID: " + bhwID);
            } else {
                System.out.println("No BHW record found with BHW ID: " + bhwID);
            }
            conn.close();

            return rowsUpdated;
    
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    

	//deletes bhw row given bhw ID
	//returns 1 if successful, 0 otherwise
	public int delete_bhw_record() {
		String sql = "DELETE FROM bhw WHERE bhwID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, bhwID);

            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("BHW record deleted successfully for BHW ID: " + bhwID);

                return 1; // deletion successful
            } else {
                System.out.println("No BHW record found with BHW ID: " + bhwID);
                return 0; // no record found to delete
            }

            

        } catch (SQLException e) {
            e.printStackTrace();
            return 0; // deletion failed due to an exception
        }
	}

	//get barangay no. 
	//returns the barangayID, given the barangayName if it exists
	//otherwise, -1
	public static int getBrgyNo(String brgyName) {
		String sql = "SELECT barangayID FROM barangay WHERE barangayName = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, brgyName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {

                return rs.getInt("barangayID");
            } else {

                return -1; 
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Return -1 in case of an error/no barangay found
        }
	} 


    public static String getBrgyName(int brgyID) {
        String sql = "SELECT barangayName FROM barangay WHERE barangayID = ?";
        String barangayName = null;
    
        try (
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setInt(1, brgyID); // Set the barangayID parameter
    
            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {
                    barangayName = rs.getString("barangayName"); // Retrieve the barangayName
                } else {
                    System.out.println("No barangay found with ID: " + brgyID);
                }
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return barangayName; // Return the barangay name or null if not found
    }
    
}

