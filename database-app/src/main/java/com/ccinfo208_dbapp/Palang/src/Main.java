package com.ccinfo208_dbapp.Palang.src;

import java.sql.*;
import java.text.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        String url = "jdbc:mysql://153.92.15.3:3306/u400425564_ccinfo208db";
        String user = "u400425564_root";
        String password = "DLSUm1234!";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Scanner scanner = new Scanner(System.in)) {

            while (true) {
                // menu for choosing between accessing patient records or generating frequency reports
                System.out.println("\nChoose a program:");
                System.out.println("1. View Patient Records");
                System.out.println("2. Generate Frequency Report by Field");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        viewPatientRecords(scanner, connection);
                        break;
                    case 2:
                        generateFrequencyReport(scanner, connection);
                        break;
                    case 3:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice, please try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void viewPatientRecords(Scanner scanner, Connection connection) throws SQLException {
        System.out.print("Enter patientID: ");
        int patientID = scanner.nextInt();
        scanner.nextLine();

        // query for patient data
        String patientQuery = "SELECT * FROM patient WHERE patientID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(patientQuery)) {
            stmt.setInt(1, patientID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // displays the patient record
                System.out.println("\nPatient record:");

                // concatenates first name, middle name, and last name to form the full name
                String firstName = rs.getString("firstname");
                String middleName = rs.getString("middlename");
                String lastName = rs.getString("lastname");
                String fullName = (firstName != null ? firstName : "") +
                                  (middleName != null ? " " + middleName : "") +
                                  (lastName != null ? " " + lastName : "");

                System.out.println("Name: " + (fullName.trim().isEmpty() ? "N/A" : fullName.trim()));

                // handles possible NULL values in fields
                String birthday = rs.getString("birthday");
                System.out.println("Birthday: " + (birthday != null ? birthday : "N/A"));

                String sex = rs.getString("sex");
                System.out.println("Sex: " + (sex != null ? sex : "N/A"));

                String isBcgVaccinated = rs.getString("is_bcg_vaccinated");
                System.out.println("Is the patient BCG vaccinated: " + (isBcgVaccinated != null ? isBcgVaccinated : "N/A"));

                int addressID = rs.getInt("addressID");
                System.out.println("Address ID: " + (rs.wasNull() ? "N/A" : addressID));

                int closecontactcaseID = rs.getInt("closecontactcaseID"); 
                System.out.println("Close contact case ID: " + (rs.wasNull() ? "N/A" : closecontactcaseID));
            } else {
                System.out.println("No patient found with patientID: " + patientID);
            }
        }

        // prompts user to press any key before returning to the main menu
        System.out.println("\nPress any key to return to the main menu...");
        scanner.nextLine(); 
    }

    private static void generateFrequencyReport(Scanner scanner, Connection connection) throws SQLException {
        System.out.println("\nAvailable tables for frequency reports:");
        System.out.println("1. Patients");
        System.out.println("2. Patient medical cases");
        System.out.print("Enter the number corresponding to the chosen table: ");
        int selectedTableIndex = scanner.nextInt();
        scanner.nextLine();

        // gets the table name based on user's selection
        String selectedTable = null;
        switch (selectedTableIndex) {
            case 1:
                selectedTable = "patient";
                break;
            case 2:
                selectedTable = "patient_case";
                break;
            default:
                System.out.println("Invalid selection. Please try again.");
                return;
        }

        if (selectedTable != null) {
            System.out.println("\nTable selected: " + selectedTable);

            // queries the columns of the selected table
            List<String> allowedFields = new ArrayList<>();
            if ("patient".equals(selectedTable)) {
                allowedFields.add("birthday");
                allowedFields.add("sex");
            } else if ("patient_case".equals(selectedTable)) {
                allowedFields.add("start_date");
                allowedFields.add("end_date");
                allowedFields.add("diagnosis_date");
                allowedFields.add("finalDiagnosis");
            }

            // lets the user select a field
            System.out.println("\nAvailable fields:");
            for (int i = 0; i < allowedFields.size(); i++) {
                System.out.println((i + 1) + ". " + allowedFields.get(i));
            }

            System.out.print("Enter the number corresponding to the chosen field: ");
            int fieldSelection = scanner.nextInt();
            scanner.nextLine();

            if (fieldSelection < 1 || fieldSelection > allowedFields.size()) {
                System.out.println("Invalid selection. Please try again.");
                return;
            }

            String fieldName = allowedFields.get(fieldSelection - 1);
            System.out.println("\nYou selected the field: " + fieldName);

            // generates frequency report based on field selection
            if ("patient".equals(selectedTable) && "birthday".equals(fieldName)) {
                generateAgeFrequencyReport(scanner, connection);
            } else if ("patient".equals(selectedTable) && "sex".equals(fieldName)) {
                generateSexFrequencyReport(scanner, connection);
            } else if ("patient_case".equals(selectedTable) && "finalDiagnosis".equals(fieldName)) {
                generateFinalDiagnosisFrequencyReport(connection); 
            } else if ("patient_case".equals(selectedTable)) {
                generateDateFrequencyReport(fieldName, connection);
            }
        }

        System.out.println("\nPress any key to return to the main menu.");
        scanner.nextLine(); 
    }

    private static void generateDateFrequencyReport(String fieldName, Connection connection) throws SQLException {
        String query = "SELECT " + fieldName + " FROM patient_case";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            Map<String, Integer> dateFrequency = new TreeMap<>(); // TreeMap ensures sorting by key (date)

            // parses and formats date
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMMM yyyy");

            while (rs.next()) {
                String dateStr = rs.getString(fieldName);
                if (dateStr != null) {
                    try {
                        java.util.Date date = df.parse(dateStr);
                        String formattedDate = new SimpleDateFormat("yyyy-MM").format(date);
                        String monthYear = monthYearFormat.format(date);
                        String dateKey = formattedDate + " (" + monthYear + ")";
                        dateFrequency.put(dateKey, dateFrequency.getOrDefault(dateKey, 0) + 1);
                    } catch (ParseException e) {
                        System.out.println("Error parsing date: " + dateStr);
                    }
                }
            }

            // print dates frequency report
            System.out.println("\n" + fieldName + " Frequency report (grouped by month/year):");
            dateFrequency.forEach((key, value) -> {
                System.out.println(key + " | " + value);
            });
        }
    }

    private static void generateAgeFrequencyReport(Scanner scanner, Connection connection) throws SQLException {
        // query for patient data to calculate ages
        String query = "SELECT birthday FROM patient";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            Map<Integer, Integer> ageFrequency = new HashMap<>();

            while (rs.next()) {
                String birthdayStr = rs.getString("birthday");
                if (birthdayStr != null) {
                    try {
                        // calculates the age based on birthday
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        java.util.Date birthday = df.parse(birthdayStr);
                        int age = calculateAge(birthday);

                        ageFrequency.put(age, ageFrequency.getOrDefault(age, 0) + 1);
                    } catch (ParseException e) {
                        System.out.println("Error parsing birthday: " + birthdayStr);
                    }
                }
            }

            // prints age frequency report
            System.out.println("\nAge frequency report:");
            ageFrequency.forEach((age, count) -> System.out.println(age + " | " + count));
            System.out.print("Enter age to generate report for: ");
            int selectedAge = scanner.nextInt();
            scanner.nextLine();
            generateFinalDiagnosisFrequencyReportByAge(selectedAge, connection);
        }
    }

    private static void generateSexFrequencyReport(Scanner scanner, Connection connection) throws SQLException {
        // query for patient sex data
        String query = "SELECT sex FROM patient";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            Map<String, Integer> sexFrequency = new HashMap<>();

            while (rs.next()) {
                String sex = rs.getString("sex");
                if (sex != null) {
                    sexFrequency.put(sex, sexFrequency.getOrDefault(sex, 0) + 1);
                }
            }

            // prints sex frequency report
            System.out.println("\nSex frequency report:");
            sexFrequency.forEach((sex, count) -> System.out.println(sex + " | " + count));
            System.out.print("Enter sex (M/F) to generate report for: ");
            String selectedSex = scanner.nextLine().toUpperCase();
            generateFinalDiagnosisFrequencyReportBySex(selectedSex, connection);
        }
    }

    private static void generateFinalDiagnosisFrequencyReport(Connection connection) throws SQLException {
        String query = "SELECT finalDiagnosis FROM patient_case";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            Map<String, Integer> diagnosisFrequency = new HashMap<>();
            while (rs.next()) {
                String finalDiagnosis = rs.getString("finalDiagnosis");
                if (finalDiagnosis != null) {
                    diagnosisFrequency.put(finalDiagnosis, diagnosisFrequency.getOrDefault(finalDiagnosis, 0) + 1);
                }
            }

            System.out.println("\nFinal diagnosis frequency report:");
            diagnosisFrequency.forEach((diagnosis, count) -> System.out.println(diagnosis + " | " + count));
        }
    }

    private static void generateFinalDiagnosisFrequencyReportByAge(int selectedAge, Connection connection) throws SQLException {
        String query = "SELECT finalDiagnosis FROM patient_case WHERE patientID IN (SELECT patientID FROM patient WHERE YEAR(CURDATE()) - YEAR(birthday) = ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, selectedAge);
            try (ResultSet rs = stmt.executeQuery()) {
                Map<String, Integer> diagnosisFrequency = new HashMap<>();
                while (rs.next()) {
                    String finalDiagnosis = rs.getString("finalDiagnosis");
                    if (finalDiagnosis != null) {
                        diagnosisFrequency.put(finalDiagnosis, diagnosisFrequency.getOrDefault(finalDiagnosis, 0) + 1);
                    }
                }

                System.out.println("\nFinal diagnosis frequency report by age " + selectedAge + ":");
                diagnosisFrequency.forEach((diagnosis, count) -> System.out.println(diagnosis + " | " + count));
            }
        }
    }

    private static void generateFinalDiagnosisFrequencyReportBySex(String selectedSex, Connection connection) throws SQLException {
        String query = "SELECT finalDiagnosis FROM patient_case WHERE patientID IN (SELECT patientID FROM patient WHERE sex = ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, selectedSex);
            try (ResultSet rs = stmt.executeQuery()) {
                Map<String, Integer> diagnosisFrequency = new HashMap<>();
                while (rs.next()) {
                    String finalDiagnosis = rs.getString("finalDiagnosis");
                    if (finalDiagnosis != null) {
                        diagnosisFrequency.put(finalDiagnosis, diagnosisFrequency.getOrDefault(finalDiagnosis, 0) + 1);
                    }
                }

                System.out.println("Final diagnosis frequency report by sex " + selectedSex + ":");
                diagnosisFrequency.forEach((diagnosis, count) -> System.out.println(diagnosis + " | " + count));
            }
        }
    }

    private static int calculateAge(java.util.Date birthday) {
        Calendar today = Calendar.getInstance();
        Calendar birthDate = Calendar.getInstance();
        birthDate.setTime(birthday);

        int age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
        if (today.get(Calendar.MONTH) < birthDate.get(Calendar.MONTH) ||
                (today.get(Calendar.MONTH) == birthDate.get(Calendar.MONTH) && today.get(Calendar.DATE) < birthDate.get(Calendar.DATE))) {
            age--;
        }
        return age;
    }
}
