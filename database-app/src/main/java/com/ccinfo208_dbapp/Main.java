package com.ccinfo208_dbapp;

import java.util.*;
import java.sql.*;
import com.ccinfo208_dbapp.Barcelita.*;
import com.ccinfo208_dbapp.Mortega.*;
import com.ccinfo208_dbapp.Palang.*;
import com.ccinfo208_dbapp.Cruz.*;

public class Main {
	public Main () {

	}

	public void run() {
		Scanner sc = new Scanner(System.in);
	    int choice = -1;
		Connection connection = null;

		try {
			connection = MySQLConnection.getConnection();

			do {
				System.out.println("=======================================================           ");
				System.out.println("                Application Main Menu			                    ");
				System.out.println("-------------------------------------------------------           ");
				System.out.println("[1] Patient Record Management");
				System.out.println("[2] Barangay Record Management");
				System.out.println("[3] Barangay Physician Record Management");
				System.out.println("[4] Barangay Health Worker Record Management");
				System.out.println("[5] Case Registration and Closing");
				System.out.println("[6] Health Assessment, Laboratory Result Submission and Diagnosis");
				System.out.println("[7] Registration and Monitoring of Direct Patient Contacts");
				System.out.println("[8] Prescription Processing");
				System.out.println("[9] Report Generation - Final Diagnosis Frequency Report");
				System.out.println("[10] Report Generation - Health Assessment Frequency Report");
				System.out.println("[11] Report Generation - Direct Contact Monitoring Productivity Report");
				System.out.println("[12] Report Generation - Case Productivity");
				System.out.println("[0] Exit Application");
				System.out.println("=======================================================");
	
				choice = sc.nextInt();
	
				switch (choice) {
					case 1:
						
						break;
					case 2:
						 
						break;
					case 3:
						
						break;
					case 4:
						// AppController.bhw_rm_controller(sc); //BHW Record Management
						break;
					case 5:
						
						break;
					case 6:
						
						break;
					case 7:
						 
						break;
					case 8:
						// AppController.prtn_controller(sc); 
						break;
					case 9:
						
						break;
					case 10:
						
						 break;
					case 11:
						 
						break;
					case 12:
						// AppController.cpr_controller(sc); //case productivity report
						break;
					case 0:
						System.out.println("Exiting...");
						connection.close();
						break;
					default:
						System.out.println("Invalid choice. Please try again.");
					}
				} while (choice != 0);
	
				sc.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Main app = new Main();
		app.run();
	}
}


