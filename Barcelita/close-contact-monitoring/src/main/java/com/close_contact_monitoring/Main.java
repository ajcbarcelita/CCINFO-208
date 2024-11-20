package com.close_contact_monitoring;

import java.util.*; //Scanner and InputMismatchException
import java.sql.*;
import com.close_contact_monitoring.utility.*;

public class Main {
	//why make DAOs as attributes of main?
	private Scanner sc;

	// ANSI escape codes for text colors
    public static final String RESET = "\033[0m";      // Reset color
    public static final String GREEN = "\033[0;32m";  // Green
    public static final String RED = "\033[0;31m";    // Red
	public static final String ORANGE = "\033[38;5;214m"; // Orange 
    public static final String BLUE = "\033[0;34m";   // Blue

	public Main() {
		sc = new Scanner(System.in);
	}
	
	private void managePatientRecord() {
		
	}
	
	private void registerAndMonitorPatientContacts() {
		
	}
	
	private void generateContactMonitoringReport() {
		
	}
	
	private void run() {
		boolean exitApp = false;
		int choice = -1;
		
		Connection connection = null;
		try {
			connection = MySQLConnection.getConnection(); // Establish the connection here
		} catch (SQLException e) {
			sc.close();
			return; // Exit the method if the connection fails
		}
		
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
		
		Main app = new Main();
		app.run();
	}
}
