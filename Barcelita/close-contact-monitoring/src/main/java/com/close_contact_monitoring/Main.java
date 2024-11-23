package com.close_contact_monitoring;

import java.util.*; //Scanner and InputMismatchException
import java.sql.*;
import com.close_contact_monitoring.model.*;
import com.close_contact_monitoring.dao.*;
import com.close_contact_monitoring.utility.*;

public class Main {
	//why make DAOs as attributes of main?
	private Scanner sc;
	private PatientDAO patientDAO;
	private AddressDAO addressDAO;
	private CloseContactDAO barangayDAO;
	private ComorbidityDAO comorbidityDAO;
	private GuardianDAO guardianDAO;

	// ANSI escape codes for text colors
    public static final String RESET = "\033[0m";      // Reset color
    public static final String GREEN = "\033[0;32m";  // Green
    public static final String RED = "\033[0;31m";    // Red
	public static final String ORANGE = "\033[38;5;214m"; // Orange 
    public static final String BLUE = "\033[0;34m";   // Blue

	public Main(PatientDAO patientDAO, AddressDAO addressDAO, CloseContactDAO barangayDAO, ComorbidityDAO comorbidityDAO, GuardianDAO guardianDAO) {
		this.sc = new Scanner(System.in);
		this.patientDAO = patientDAO;
		this.addressDAO = addressDAO;
		this.barangayDAO = barangayDAO;
		this.comorbidityDAO = comorbidityDAO;
		this.guardianDAO = guardianDAO;
	}
	
	private void managePatientCoreData() {
        boolean exitSubMenu = false;

        while (!exitSubMenu) {
            System.out.println(ORANGE + "\nManage Patient Core Data\n" + RESET);
            System.out.println("[1] Create Patient Record."); 
            System.out.println("[2] View Patient Record using Patient ID.");
            System.out.println("[3] Update Patient Record.");
            System.out.println("[4] (Soft) Delete Patient Record.\n"); //WIP
			System.out.println("[5] Add Guardian to Patient Record.");
			System.out.println("[6] View Guardian/s of Patient Record.");
			System.out.println("[7] Delete a Guardian of Patient Record.\n"); //WIP
            System.out.println("[8] Add Comorbidity to Patient Record.");
			System.out.println("[9] Add Comorbidity to Reference Table.");
            System.out.println("[10] View Comorbidities of Patient Record.");
			System.out.println("[11] Delete a Comorbidity of Patient Record.\n"); //WIP
            System.out.println("[12] Add Patient TB Case to Patient Record."); //WIP
			System.out.println("[13] View Patient TB Cases of Patient Record."); //WIP
			System.out.println("[14] (Soft) Delete a Patient TB Case of Patient Record.\n"); //WIP
			System.out.println("[15] View a Patient Record and All Associated Records."); //WIP
			System.out.println("[16] Back to Main Menu.");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1:
					// Add your code for creating a patient record here
					System.out.println(ORANGE + "\nCreate Patient Record selected.\n" + RESET);
					patientDAO.addPatient();
					break;
				case 2:
					// Add your code for viewing a patient record using patient ID here
					System.out.println(ORANGE + "\nView a Patient Record using Patient ID selected.\n" + RESET);
					int patientID = InputUtility.getIntInput("Enter Patient ID: ");
					Patient patient = patientDAO.getPatientByID(patientID);
					if (patient != null) {
						patientDAO.printPatientDetails(patient);
					} else {
						System.out.println("Patient not found.");
					}
					break;
				case 3:
					// Add your code for updating a patient record here
					System.out.println(ORANGE + "Update Patient Record selected." + RESET);
					int toupdate_patientID = InputUtility.getIntInput("Enter Patient ID: ");
					Patient toupdate_patient = patientDAO.getPatientByID(toupdate_patientID);
					if (toupdate_patient != null) {
						patientDAO.updatePatient(toupdate_patientID);
					} else {
						System.out.println("Patient not found.");
					}
					break;
				case 4:
					// Add your code for (soft) deleting a patient record here
					System.out.println("(Soft) Delete Patient Record selected. - WIP");
					break;
				case 5:
					// Add your code for adding a guardian to a patient record here
					System.out.println("Add Guardian to Patient Record selected.");
					guardianDAO.addGuardianToPatientRecord();
					break;
				case 6:
					// Add your code for viewing guardians of a patient record here
					System.out.println(ORANGE + "\nView Guardian/s of Patient Record selected.\n" + RESET);
					patientID = InputUtility.getIntInput("Enter Patient ID: ");
					guardianDAO.viewGuardiansOfPatient(patientID);
					
					break;
				case 7:
					// Add your code for deleting a guardian of a patient record here
					System.out.println("Delete a Guardian of Patient Record selected. - WIP");
					break;
				case 8:
					// Add your code for adding a comorbidity to a patient record here
					System.out.println("Add Comorbidity to Patient Record selected.");
					comorbidityDAO.addComorbidityToPatientRecord();
					break;
				case 9:
					// Add your code for adding a comorbidity to the reference table here
					System.out.println(ORANGE + "\nAdd Comorbidity to Reference Table selected.\n" + RESET);
					comorbidityDAO.addComorbidityToRefTable();
					break;
				case 10:
					// Add your code for viewing comorbidities of a patient record here
					System.out.println("\nView Comorbidities of Patient Record selected.\n");
					patientID = InputUtility.getIntInput("Enter Patient ID: ");
					comorbidityDAO.viewComorbiditiesByPatientID(patientID);
					break;
				case 11:
					// Add your code for deleting a comorbidity of a patient record here
					System.out.println("Delete a Comorbidity of Patient Record selected. - WIP");
					break;
				case 12:
					// Add your code for adding a patient TB case to a patient record here
					System.out.println("Add Patient TB Case to Patient Record selected.");
					break;
				case 13:
					// Add your code for viewing patient TB cases of a patient record here
					System.out.println("View Patient TB Cases of Patient Record selected.");
					break;
				case 14:
					// Add your code for (soft) deleting a patient TB case of a patient record here
					System.out.println("(Soft) Delete a Patient TB Case of Patient Record selected. - WIP");
					break;
				case 15:
					// Add your code for viewing a patient record and all associated records here
					System.out.println("View a Patient Record and All Associated Records selected.");
					break;
				case 16:
					exitSubMenu = true;
            }
        }
    }

    private void registerAndMonitorPatientContacts() {
        System.out.println(ORANGE + "Register and Monitor Patient Contacts\n" + RESET);
        boolean exitSubMenu = false;
        while (!exitSubMenu) {
            System.out.println(ORANGE + "Register and Monitor Patient Contacts\n" + RESET);
            System.out.println("[1] Register Contact");
            System.out.println("[2] Monitor Contact");
            System.out.println("[3] Back to Main Menu");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    // Add your code for registering a contact here
                    System.out.println("Register Contact selected.");
                    break;
                case 2:
                    // Add your code for monitoring a contact here
                    System.out.println("Monitor Contact selected.");
                    break;
                case 3:
                    exitSubMenu = true;
                    break;
                default:
                    System.out.println(RED + "Invalid choice. Please enter a valid choice.\n" + RESET);
            }
        }
    }

    private void generateContactMonitoringReport() {
        System.out.println(ORANGE + "Generate Contact Monitoring Report\n" + RESET);
        boolean exitSubMenu = false;
        while (!exitSubMenu) {
            System.out.println(ORANGE + "Generate Contact Monitoring Report\n" + RESET);
            System.out.println("[1] Generate Daily Report");
            System.out.println("[2] Generate Weekly Report");
            System.out.println("[3] Generate Monthly Report");
            System.out.println("[4] Back to Main Menu");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    // Add your code for generating a daily report here
                    System.out.println("Generate Daily Report selected.");
                    break;
                case 2:
                    // Add your code for generating a weekly report here
                    System.out.println("Generate Weekly Report selected.");
                    break;
                case 3:
                    // Add your code for generating a monthly report here
                    System.out.println("Generate Monthly Report selected.");
                    break;
                case 4:
                    exitSubMenu = true;
                    break;
                default:
                    System.out.println(RED + "Invalid choice. Please enter a valid choice.\n" + RESET);
            }
        }
    }
	
	private void run(Connection connection) {
		boolean exitApp = false;
		int choice = -1;
		
		while (!exitApp) {
			System.out.println("====================================");
			System.out.println(ORANGE + "CLOSE CONTACT MONITORING APPLICATION\n" + RESET);
			System.out.println("Main Menu:");
			System.out.println("[1] Manage Patient Records (CRUDS).");
			System.out.println("[2] Registration and Monitoring of Direct Patient Contacts.");
			System.out.println("[3] Direct Contact Monitoring Productivity Report.");
			System.out.println("[4] Exit the Program.\n");
			try {
				System.out.print("Enter choice: ");
				choice = sc.nextInt();
			} catch (InputMismatchException e){
				System.err.println(RED + "ERROR. Please enter a valid INTEGER." + RESET);
				e.printStackTrace();
				sc.next();
				continue;
			}
			
			switch(choice) {
				case 1:
					//create sub-menu later on for the management of patient records.
					System.out.println(ORANGE + "Manage Patient Records (CRUDS) selected.\n" + RESET);
					managePatientCoreData();
				break;
				
				case 2:
					//create sub-menu later on for the registration and monitoring of close contacts.
					System.out.println(ORANGE + "Registration and Monitoring of Direct Patient Contacts` selected.\n" + RESET);
				break;
					
				case 3:
					//create sub-menu later on for the creation of reports
					System.out.println(ORANGE + "Direct Contact Monitoring Productivity Report/s selected.\n" + RESET);
				break;
					
				case 4:
					exitApp = true;
					System.out.println("Exiting the application...");	
					//close the connection to the database
					MySQLConnection.closeConnection(connection);				
					sc.close();
				break;

				default:
					System.out.println(RED + "Invalid choice. Please enter a valid choice.\n" + RESET);
			}
		}
	}
	
	public static void main(String[] args) {
		//create DAO objects
		Connection connection = null;
		try {
			connection = MySQLConnection.getConnection(); // Establish the connection here
		} catch (SQLException e) {
			return; // Exit the method if the connection fails
		}

		PatientDAO patientDAO = new PatientDAO(connection);
		AddressDAO addressDAO = new AddressDAO(connection);
		CloseContactDAO barangayDAO = new CloseContactDAO(connection);
		ComorbidityDAO comorbidityDAO = new ComorbidityDAO(connection);
		GuardianDAO guardianDAO = new GuardianDAO(connection);

		Main app = new Main(patientDAO, addressDAO, barangayDAO, comorbidityDAO, guardianDAO);
		app.run(connection);
	}
}
