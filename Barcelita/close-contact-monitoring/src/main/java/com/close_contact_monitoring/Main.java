package com.close_contact_monitoring;

import java.util.*; //Scanner and InputMismatchException

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		boolean exitApp = false;
		int choice = -1;
		
		while (!exitApp) {
			System.out.println("\nClose Contact Monitoring Application\n");
			System.out.println("Main Menu:");
			System.out.println("[1] Manage Patient Records (CRUD).");
			System.out.println("[2] Registration and Monitoring of Direct Patient Contacts.");
			System.out.println("[3] Direct Contact Monitoring Productivity Report.");
			System.out.println("[4] Exit the Program.\n");
			try {
				System.out.print("Enter choice: ");
				choice = sc.nextInt();
			} catch (InputMismatchException e){
				System.err.println("ERROR. Please enter a valid INTEGER.");
				e.printStackTrace();
			}
			
			switch(choice) {
				case 1:
					//create sub-menu later on for the management of patient records.
				break;
				
				case 2:
					//create sub-menu later on for the registration and monitoring of close contacts.
				break;
					
				case 3:
					//create sub-menu later on for the creation of reports
				break;
					
				case 4:
					exitApp = true;
					System.out.println("Exiting the application...");					
					sc.close();
				break;

				default:
					System.out.println("Invalid choice. Please enter a valid choice.\n");
			}
		}
	}

}
