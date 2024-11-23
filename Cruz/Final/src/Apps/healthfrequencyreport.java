package Apps;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class healthfrequencyreport {
    private static final String DB_URL = "jdbc:mysql://153.92.15.3:3306/u400425564_ccinfo208db";
    private static final String DB_USER = "u400425564_root";
    private static final String DB_PASSWORD = "DLSUm1234!";

    public String resultID;
    public int healthassessmentID;
    public String medicalcenterID;
    public String test_typeID;
    public String test_date; // Example: "YYYY-MM-DD"
    public String test_result;
    public String finalDiagnosis;
    public String comments;
    public int month;
    public int year;

    public healthfrequencyreport() {
        this.resultID = "";
        this.healthassessmentID = 0;
        this.medicalcenterID = "";
        this.test_typeID = "";
        this.test_date = "";
        this.test_result = "";
        this.comments = "";
        this.month = 1;
        this.year = 2024;
    }

    // Health Assessment Frequency Report per Final Diagnosis
    public int healthAssessmentFrequencyReport() {
        String query = """
            SELECT COUNT(*) AS frequency
            FROM healthassessment
            WHERE test_result = ?
            AND MONTH(test_date) = ?
            AND YEAR(test_date) = ?
        """;

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            stmt = conn.prepareStatement(query);
            stmt.setString(1, finalDiagnosis);
            stmt.setString(2, test_date);
            rs = stmt.executeQuery();

            // Return the frequency of health assessments with the given final diagnosis
            if (rs.next()) {
                return rs.getInt("frequency");
            } else {
                return 0; // No health assessments found for the given criteria
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Error occurred
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

