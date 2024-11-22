package ccinfom_mco;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


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
		String sql = "INSERT INTO bhw (bhwID, lastname, firstname, middlename, barangay_assignedto) VALUES (?, ?, ?, ?, ?)";

		try {
			Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
			System.out.println("Connection to DB Successful");
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			//BHW_ID is auto-incremented
			pstmt.setString(1, bhw_lastName);
			pstmt.setString(2, bhw_firstName);
			pstmt.setString(3, bhw_middleName);
			pstmt.setInt(4, getBrgyNo(barangayAssignedTo)); //we already made sure in the controller that the input for the barangay will always be storage valid for the database
			System.out.println("SQL Statement Prepared");

			int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new BHW was inserted successfully!");

                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        System.out.println("The ID assigned to the BHW is: " + generatedId);
                    }
                }
            }

			pstmt.close();
			conn.close();
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
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return exists;
    }

	//updates existing bhw record
	//returns 1 if successful. 0, otherwise
	public int update_bhw_record() {
        StringBuilder sql = new StringBuilder("UPDATE bhw SET ");
        boolean isFirstField = true;

        if (bhw_lastName != null) {
            sql.append("lastname = ?");
            isFirstField = false;
        }
        if (bhw_firstName != null) {
            if (!isFirstField) sql.append(", ");
            sql.append("firstname = ?");
            isFirstField = false;
        }
        if (bhw_middleName != null) {
            if (!isFirstField) sql.append(", ");
            sql.append("middlename = ?");
            isFirstField = false;
        }
        if (barangayAssignedTo != null) { 
            if (!isFirstField) sql.append(", ");
            sql.append("barangay_assignedto = ?");
        }

        sql.append(" WHERE bhwID = ?");

        if (isFirstField) {
            System.out.println("No fields to update for BHW ID: " + bhwID);
            return 0;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            int parameterIndex = 1;

            if (bhw_lastName != null) {
                pstmt.setString(parameterIndex++, bhw_lastName);
            }
            if (bhw_firstName != null) {
                pstmt.setString(parameterIndex++, bhw_firstName);
            }
            if (bhw_middleName != null) {
                pstmt.setString(parameterIndex++, bhw_middleName);
            }
            if (barangayAssignedTo != null) {
                pstmt.setInt(parameterIndex++, getBrgyNo(barangayAssignedTo)); //if N/A, will not be updated
            }

            pstmt.setInt(parameterIndex, bhwID);

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("BHW record updated successfully for BHW ID: " + bhwID);
            } else {
                System.out.println("No BHW record found with BHW ID: " + bhwID);
            }
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
}

