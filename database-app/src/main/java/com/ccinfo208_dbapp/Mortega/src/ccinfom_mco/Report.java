package com.ccinfo208_dbapp.Mortega.src.ccinfom_mco;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Report {
        private static final String DB_URL = "jdbc:mysql://153.92.15.3:3306/u400425564_ccinfo208db";
        private static final String DB_USER = "u400425564_root";
        private static final String DB_PASSWORD = "DLSUm1234!";

        public String bhw_firstName;
        public String bhw_lastName;
        public String bhw_middleName;
        public String bhw_barangay;
        public int bhwID;
        public int physicianPRCNo;
        public int month;
        public int year;

        public Report() {
                this.bhw_firstName = "";
                this.bhw_lastName = "";
                this.bhw_middleName = "";
                this.bhw_barangay = "";
                this.bhwID = 0;
                this.physicianPRCNo = 0;
                this.month = 1;
                this.year = 2024;
        }

        /*
         Returns the bhwID if a match is found.
         Returns 0 if no matching BHW is found.
         Returns -1 if an SQL error occurs.
         */
        public int getBHWID() {
                String query = "SELECT bhwID FROM bhw WHERE lastname = ? AND firstname = ? AND middlename = ? AND barangay_assignedto = ?";
                Connection conn = null;
                PreparedStatement stmt = null;
                ResultSet rs = null;

                try {
                        conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

                        stmt = conn.prepareStatement(query);
                        stmt.setString(1, bhw_lastName);
                        stmt.setString(2, bhw_firstName);
                        stmt.setString(3, bhw_middleName);
                        stmt.setString(4, bhw_barangay);

                        rs = stmt.executeQuery();

                        if (rs.next()) {
                        return rs.getInt("bhwID");
                        } else {

                        System.out.println("No matching BHW found.");
                        return 0;
                        }
                } catch (SQLException e) {
                        e.printStackTrace();
                        return -1; //error
                } finally {
                        try {
                                
                        if (rs != null) rs.close();
                        if (stmt != null) stmt.close();
                        if (conn != null) conn.close();
                        } catch (SQLException e) {
                        e.printStackTrace();
                        }
                }


        }


        //returns 1 if successful, 0 otherwise

        //barangayHealthWorker report
        //bhw name
        //bhw barangay
                //patient_case (number of patient cases per)
                        //month & year

        
        // Case Productivity Report per Barangay Health Worker
        public int bhw_cp_report() {
                String query = """
                SELECT COUNT(*) AS case_count 
                FROM patient_case 
                WHERE involved_bhw = ? 
                AND MONTH(start_date) = ? 
                AND YEAR(start_date) = ?
                """;
                
                Connection conn = null;
                PreparedStatement stmt = null;
                ResultSet rs = null;

                try {

                conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

                stmt = conn.prepareStatement(query);
                stmt.setInt(1, bhwID);
                stmt.setInt(2, month);
                stmt.setInt(3, year);
                rs = stmt.executeQuery();

                // return the count of cases
                if (rs.next()) {
                        return rs.getInt("case_count");
                } else {
                        return 0; // no cases 
                }
                } catch (SQLException e) {
                e.printStackTrace();
                return -1; // error
                } finally {
                try {
                        if (rs != null) rs.close();
                        if (stmt != null) stmt.close();
                        if (conn != null) conn.close();

                } catch (SQLException e) {
                        e.printStackTrace();
                }
                }
        }



        // Case Productivity Report per Physician
        public int physician_cp_report() {
                String query = """
                SELECT COUNT(*) AS case_count 
                FROM patient_case 
                WHERE involved_physician = ? 
                AND MONTH(start_date) = ? 
                AND YEAR(start_date) = ?
                """;

                Connection conn = null;
                PreparedStatement stmt = null;
                ResultSet rs = null;

                try {
                if (physicianPRCNo <= 0) {
                        System.out.println("Invalid Physician PRC License Number.");
                        return 0;
                }

                conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

                stmt = conn.prepareStatement(query);
                stmt.setInt(1, physicianPRCNo);
                stmt.setInt(2, month);
                stmt.setInt(3, year);
                rs = stmt.executeQuery();

                // return count of cases
                if (rs.next()) {
                        return rs.getInt("case_count");
                } else {
                        return 0; // no cases found
                }
                } catch (SQLException e) {
                e.printStackTrace();
                return -1; // error
                } finally {
                try {

                        if (rs != null) rs.close();
                        if (stmt != null) stmt.close();
                        if (conn != null) conn.close();
                } catch (SQLException e) {
                        e.printStackTrace();
                }
                }
        }

}

