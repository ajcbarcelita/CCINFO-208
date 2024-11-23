import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.Map;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    // Database connection parameters
    static final String DB_URL = "jdbc:mysql://153.92.15.3:3306/u400425564_ccinfo208db";
    static final String USER = "u400425564_root";
    static final String PASSWORD = "DLSUm1234!";

    // Scanner for user input
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Display main menu for the user
        while (true) {
            System.out.println("Select an option:");
            System.out.println("1. View/Manage Physician Records");
            System.out.println("2. Generate Frequency Report");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    physicianMenu();
                    break;
                case 2:
                    generateFrequencyReport();
                    break;
                case 3:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }

    // Method to handle the physician record menu
    public static void physicianMenu() {
        while (true) {
            System.out.println("\nPhysician Menu:");
            System.out.println("1. Create New Physician Record");
            System.out.println("2. View Physician Record");
            System.out.println("3. Update Physician Record");
            System.out.println("4. Delete Physician Record");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    createPhysicianRecord();
                    break;
                case 2:
                    viewPhysicianRecord();
                    break;
                case 3:
                    updatePhysicianRecord();
                    break;
                case 4:
                    deletePhysicianRecord();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }

    // Method to create a new physician record
    public static void createPhysicianRecord() {
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter middle name: ");
        String middleName = scanner.nextLine();

        // Generate a random barangayID
        int barangayAssignedTo = 1001 + (int) (Math.random() * 10); // Random value between 1001 and 1010

        // Generate PRC ID in the format 100mnnn
        String prcId = generatePhysicianPrcId(barangayAssignedTo);

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String sql = "INSERT INTO physician (physician_prc_id, lastname, firstname, middlename, barangay_assignedto) " +
                        "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, prcId);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, firstName);
            preparedStatement.setString(4, middleName);
            preparedStatement.setInt(5, barangayAssignedTo);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Physician record created successfully with PRC ID: " + prcId);
            }
        } catch (SQLException e) {
            System.out.println("Error creating physician record: " + e.getMessage());
        }
    }

    // Method to generate a new PRC ID in the format 100mnnn
    public static String generatePhysicianPrcId(int barangayAssignedTo) {
        // Extract the last digit of the barangay ID (m)
        int lastDigitOfBarangay = barangayAssignedTo % 10;
        
        // Get the last PRC ID generated for this barangay
        int lastPrcId = getLastPrcIdForBarangay(barangayAssignedTo);

        // Increment the PRC ID number (nnn), starting from 004
        int nextId = lastPrcId + 1;
        String formattedId = String.format("%03d", nextId); // Format the last three digits (nnn)

        // Generate the PRC ID in the format 100mnnn
        return "100" + lastDigitOfBarangay + formattedId;
    }

    // Method to get the last PRC ID generated for a specific barangay
    public static int getLastPrcIdForBarangay(int barangayAssignedTo) {
        int lastPrcId = 3; // Starting point (the first PRC ID for any barangay is 004, so 3 is the initial number)

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String sql = "SELECT MAX(physician_prc_id) FROM physician WHERE barangay_assignedto = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, barangayAssignedTo);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String maxPrcId = resultSet.getString(1);
                if (maxPrcId != null && !maxPrcId.isEmpty()) {
                    // Extract the last 3 digits (nnn) of the PRC ID
                    lastPrcId = Integer.parseInt(maxPrcId.substring(4));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting last PRC ID for barangay: " + e.getMessage());
        }

        return lastPrcId;
    }


    // Method to view a physician record with the assigned barangay name
    public static void viewPhysicianRecord() {
        System.out.print("Enter physician PRC ID to view: ");
        int physicianPrcId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            // SQL query to join physician table with barangay table
            String sql = "SELECT p.physician_prc_id, p.lastname, p.firstname, p.middlename, b.barangayName " +
                         "FROM physician p " +
                         "JOIN barangay b ON p.barangay_assignedto = b.barangayID " +
                         "WHERE p.physician_prc_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, physicianPrcId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                System.out.println("Physician Record:");
                System.out.println("PRC ID: " + resultSet.getInt("physician_prc_id"));
                System.out.println("Name: " + resultSet.getString("lastname") + ", " +
                                   resultSet.getString("firstname") + " " +
                                   resultSet.getString("middlename"));
                System.out.println("Barangay Assigned To: " + resultSet.getString("barangayName"));
            } else {
                System.out.println("No physician found with PRC ID: " + physicianPrcId);
            }
        } catch (SQLException e) {
            System.out.println("Error viewing physician record: " + e.getMessage());
        }
    }

    // Method to update a physician record
    public static void updatePhysicianRecord() {
        System.out.print("Enter physician PRC ID to update: ");
        int physicianPrcId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String sql = "SELECT * FROM physician WHERE physician_prc_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, physicianPrcId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                System.out.println("Current Record:");
                System.out.println("Name: " + resultSet.getString("lastname") + ", " +
                                   resultSet.getString("firstname") + " " +
                                   resultSet.getString("middlename"));
                System.out.println("Barangay Assigned To: " + resultSet.getInt("barangay_assignedto"));

                System.out.print("Enter new last name (leave blank to keep current): ");
                String lastName = scanner.nextLine();
                System.out.print("Enter new first name (leave blank to keep current): ");
                String firstName = scanner.nextLine();
                System.out.print("Enter new middle name (leave blank to keep current): ");
                String middleName = scanner.nextLine();

                // If fields are empty, retain current values
                if (lastName.isEmpty()) lastName = resultSet.getString("lastname");
                if (firstName.isEmpty()) firstName = resultSet.getString("firstname");
                if (middleName.isEmpty()) middleName = resultSet.getString("middlename");

                System.out.print("Enter new barangay assigned (leave blank to keep current): ");
                String barangayInput = scanner.nextLine();
                int barangayAssignedTo = resultSet.getInt("barangay_assignedto");
                if (!barangayInput.isEmpty()) {
                    barangayAssignedTo = Integer.parseInt(barangayInput);
                }

                String updateSql = "UPDATE physician SET lastname = ?, firstname = ?, middlename = ?, barangay_assignedto = ? " +
                                   "WHERE physician_prc_id = ?";
                PreparedStatement updateStmt = connection.prepareStatement(updateSql);
                updateStmt.setString(1, lastName);
                updateStmt.setString(2, firstName);
                updateStmt.setString(3, middleName);
                updateStmt.setInt(4, barangayAssignedTo);
                updateStmt.setInt(5, physicianPrcId);

                int rowsAffected = updateStmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Physician record updated successfully.");
                }
            } else {
                System.out.println("No physician found with PRC ID: " + physicianPrcId);
            }
        } catch (SQLException e) {
            System.out.println("Error updating physician record: " + e.getMessage());
        }
    }

    // Method to delete a physician record
    public static void deletePhysicianRecord() {
        System.out.print("Enter physician PRC ID to delete: ");
        int physicianPrcId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String sql = "DELETE FROM physician WHERE physician_prc_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, physicianPrcId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Physician record deleted successfully.");
            } else {
                System.out.println("No physician found with PRC ID: " + physicianPrcId);
            }
        } catch (SQLException e) {
            System.out.println("Error deleting physician record: " + e.getMessage());
        }
    }
    
    // The frequency report methods (same as before)
    public static void generateFrequencyReport() {
        System.out.println("Select table for frequency report:");
        System.out.println("1. Patient Table");
        System.out.println("2. Patient Case Table");
        System.out.print("Enter choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (choice == 1) {
            generatePatientFrequencyReport();
        } else if (choice == 2) {
            generatePatientCaseFrequencyReport();
        } else {
            System.out.println("Invalid choice, please try again.");
        }
    }

    // Method to generate frequency report based on age or sex
    public static void generatePatientFrequencyReport() {
        System.out.println("\nGenerating Patient Frequency Report...");
        
        // Ask the user whether they want to sort by age or sex
        System.out.println("Select how to sort the report:");
        System.out.println("1. Sort by Age");
        System.out.println("2. Sort by Sex");
        System.out.print("Enter choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Maps to store frequency counts
        Map<Integer, Integer> ageFrequency = new HashMap<>();
        Map<String, Integer> sexFrequency = new HashMap<>();
        
        // Maps to store the actual age or sex for the finalDiagnosis filtering
        Map<Integer, Integer> ageMapping = new HashMap<>();
        Map<String, Integer> sexMapping = new HashMap<>();

        // Get current date for age calculation
        LocalDate currentDate = LocalDate.now();

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            // Query to fetch patient information (birthday, sex, etc.)
            String sql = "SELECT patientID, birthday, sex FROM patient WHERE birthday IS NOT NULL AND sex IS NOT NULL";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                // Extract patientID, birthday, and sex from the result
                int patientId = resultSet.getInt("patientID");
                Date birthday = resultSet.getDate("birthday");
                String sex = resultSet.getString("sex");

                // Calculate the patient's age based on their birthday
                LocalDate birthDate = birthday.toLocalDate();
                int age = Period.between(birthDate, currentDate).getYears();

                // Count frequency by age
                ageFrequency.put(age, ageFrequency.getOrDefault(age, 0) + 1);
                ageMapping.put(age, patientId); // Store patient IDs by age for later filtering

                // Count frequency by sex
                sexFrequency.put(sex, sexFrequency.getOrDefault(sex, 0) + 1);
                sexMapping.put(sex, patientId); // Store patient IDs by sex for later filtering
            }

            // Generate the report based on the user's choice
            if (choice == 1) {
                // Report sorted by age
                System.out.println("\nAge Frequency Report (sorted by age):");
                ageFrequency.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey()) // Sort by age (ascending)
                    .forEach(entry -> System.out.println("Age " + entry.getKey() + ": " + entry.getValue() + " patients"));
            } else if (choice == 2) {
                // Report sorted by sex
                System.out.println("\nSex Frequency Report (sorted by sex):");
                sexFrequency.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey()) // Sort by sex (alphabetical)
                    .forEach(entry -> System.out.println("Sex " + entry.getKey() + ": " + entry.getValue() + " patients"));
            } else {
                System.out.println("Invalid choice. Please select 1 for Age or 2 for Sex.");
                return;
            }

            // Ask user if they want to generate a finalDiagnosis frequency report for a selected age or sex
            System.out.println("\nDo you want to generate a finalDiagnosis frequency report based on this selection?");
            System.out.println("1. Yes");
            System.out.println("2. No");
            System.out.print("Enter choice: ");
            int reportChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (reportChoice == 1) {
                if (choice == 1) {
                    // Ask user to select an age for the finalDiagnosis report
                    System.out.print("Enter age to generate finalDiagnosis frequency report: ");
                    int selectedAge = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    // Generate finalDiagnosis frequency report for the selected age
                    generateFinalDiagnosisReportByAge(selectedAge);
                } else if (choice == 2) {
                    // Ask user to select a sex for the finalDiagnosis report
                    System.out.print("Enter sex ('M' or 'F') to generate finalDiagnosis frequency report: ");
                    String selectedSex = scanner.nextLine();

                    // Generate finalDiagnosis frequency report for the selected sex
                    generateFinalDiagnosisReportBySex(selectedSex);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error generating patient frequency report: " + e.getMessage());
        }
    }

    public static void generateFinalDiagnosisReportByAge(int age) {
        System.out.println("\nGenerating finalDiagnosis frequency report for Age: " + age);
    
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            // SQL query to get the finalDiagnosis count for patients of a specific age
            String sql = "SELECT finalDiagnosis, COUNT(*) AS frequency " +
                         "FROM patient_case " +
                         "JOIN patient ON patient_case.patientID = patient.patientID " + // Assuming patient table has the patient details
                         "WHERE (YEAR(CURDATE()) - YEAR(patient.birthday)) = ? " + // Calculate age from date of birth
                         "GROUP BY finalDiagnosis";
    
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, age);
    
            ResultSet resultSet = preparedStatement.executeQuery();
    
            // Display the finalDiagnosis frequency report
            System.out.println("\nFinal Diagnosis Frequency Report (Age " + age + "):");
            boolean dataFound = false;
            while (resultSet.next()) {
                String finalDiagnosis = resultSet.getString("finalDiagnosis");
                int frequency = resultSet.getInt("frequency");
                System.out.println(finalDiagnosis + ": " + frequency + " occurrences");
                dataFound = true;
            }
    
            if (!dataFound) {
                System.out.println("No data found for the specified age.");
            }
    
        } catch (SQLException e) {
            System.out.println("Error generating finalDiagnosis report by age: " + e.getMessage());
        }
    }

    public static void generateFinalDiagnosisReportBySex(String sex) {
        System.out.println("\nGenerating finalDiagnosis frequency report for Sex: " + sex);
    
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            // SQL query to get the finalDiagnosis count for patients of a specific sex
            String sql = "SELECT finalDiagnosis, COUNT(*) AS frequency " +
                         "FROM patient_case " +
                         "JOIN patient ON patient_case.patientID = patient.patientID " + // Assuming patient table has the patient details
                         "WHERE patient.sex = ? " +
                         "GROUP BY finalDiagnosis";
    
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, sex);
    
            ResultSet resultSet = preparedStatement.executeQuery();
    
            // Display the finalDiagnosis frequency report
            System.out.println("\nFinal Diagnosis Frequency Report (Sex " + sex + "):");
            boolean dataFound = false;
            while (resultSet.next()) {
                String finalDiagnosis = resultSet.getString("finalDiagnosis");
                int frequency = resultSet.getInt("frequency");
                System.out.println(finalDiagnosis + ": " + frequency + " occurrences");
                dataFound = true;
            }
    
            if (!dataFound) {
                System.out.println("No data found for the specified sex.");
            }
    
        } catch (SQLException e) {
            System.out.println("Error generating finalDiagnosis report by sex: " + e.getMessage());
        }
    }    

    // Method to calculate age from birthdate
    public static int calculateAge(Date birthday) {
        Calendar birthDate = Calendar.getInstance();
        birthDate.setTime(birthday);
        Calendar today = Calendar.getInstance();

        int age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
        if (today.get(Calendar.MONTH) < birthDate.get(Calendar.MONTH) ||
            (today.get(Calendar.MONTH) == birthDate.get(Calendar.MONTH) && today.get(Calendar.DAY_OF_MONTH) < birthDate.get(Calendar.DAY_OF_MONTH))) {
            age--;
        }
        return age;
    }

    // Method to generate a frequency report for patient cases by dates (start_date, end_date, diagnosis_date) or finalDiagnosis
    public static void generatePatientCaseFrequencyReport() {
        System.out.println("Select report type to generate:");
        System.out.println("1. By Start Date");
        System.out.println("2. By End Date");
        System.out.println("3. By Diagnosis Date");
        System.out.println("4. By Final Diagnosis"); // Added option for finalDiagnosis
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String dateType = "";
        String finalDiagnosisColumn = "";

        switch (choice) {
            case 1:
                dateType = "start_date";
                break;
            case 2:
                dateType = "end_date";
                break;
            case 3:
                dateType = "diagnosis_date";
                break;
            case 4:
                finalDiagnosisColumn = "finalDiagnosis"; // New case for finalDiagnosis
                break;
            default:
                System.out.println("Invalid choice. Returning to main menu.");
                return;
        }

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String sql = "";
            
            if (!finalDiagnosisColumn.isEmpty()) {
                // Query to generate frequency report based on finalDiagnosis
                sql = "SELECT finalDiagnosis, COUNT(*) AS frequency " +
                    "FROM patient_case GROUP BY finalDiagnosis ORDER BY frequency DESC";
            } else {
                // Query to generate frequency report based on selected date type
                sql = "SELECT DATE_FORMAT(" + dateType + ", '%Y-%m') AS monthYear, COUNT(*) AS frequency " +
                    "FROM patient_case GROUP BY monthYear ORDER BY monthYear ASC";
            }

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            if (!finalDiagnosisColumn.isEmpty()) {
                // Display the report for finalDiagnosis
                System.out.println("\nFinal Diagnosis Frequency Report:");
                while (resultSet.next()) {
                    String finalDiagnosis = resultSet.getString("finalDiagnosis");
                    int frequency = resultSet.getInt("frequency");
                    System.out.println(finalDiagnosis + ": " + frequency + " cases");
                }
            } else {
                // Display the report for selected date type
                System.out.println("\n" + dateType.substring(0, 1).toUpperCase() + dateType.substring(1) + " Frequency Report:");
                while (resultSet.next()) {
                    String monthYear = resultSet.getString("monthYear");
                    int frequency = resultSet.getInt("frequency");
                    System.out.println(monthYear + ": " + frequency + " cases");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error generating patient case frequency report: " + e.getMessage());
        }
    }
}

