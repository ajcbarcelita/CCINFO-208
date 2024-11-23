package com.ccinfo208_dbapp.Barcelita;

import com.ccinfo208_dbapp.Barcelita.dao.*;
import com.ccinfo208_dbapp.Barcelita.model.*;
import com.ccinfo208_dbapp.Barcelita.utility.*;

import java.sql.*;
import java.util.*;

public class ManagePatientRecords {
    private Scanner sc;
    private PatientDAO patientDAO;
	private ComorbidityDAO comorbidityDAO;
	private GuardianDAO guardianDAO;

    public ManagePatientRecords(Connection connection, Scanner sc) {
        this.sc = sc;
        this.patientDAO = new PatientDAO(connection);
        this.comorbidityDAO = new ComorbidityDAO(connection);
        this.guardianDAO = new GuardianDAO(connection);
    }

    public void run() {
        boolean exitSubMenu = false;
        ArrayList<Patient> patients = new ArrayList<>();
        boolean boolElevatedFromCloseContact = false;

        while (!exitSubMenu) {
            System.out.println( "\nManage Patient Core Data\n");
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
			System.out.println("[12] View a Patient Record and All Associated Records."); //WIP
            System.out.println("[13] List Patient Records That Are Vaccinated / Not Vaccinated."); 
            System.out.println("[14] List Patient Records That Are / Are Not Elevated From Close Contacts.");
			System.out.println("[15] Back to Main Menu.");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1:
					// Add your code for creating a patient record here
					System.out.println("\nCreate Patient Record selected.\n");
					patientDAO.addPatient();
					break;
				case 2:
					// Add your code for viewing a patient record using patient ID here
					System.out.println("\nView a Patient Record using Patient ID selected.\n");
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
					System.out.println("Update Patient Record selected.");
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
					System.out.println( "\nView Guardian/s of Patient Record selected.\n");
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
					System.out.println("\nAdd Comorbidity to Reference Table selected.\n");
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
                    // Add your code for viewing a patient record and all associated records here
                    System.out.println("View a Patient Record and All Associated Records selected.");
                    patientID = InputUtility.getIntInput("Enter Patient ID: ");
                    patientDAO.getPatientAndRelatedRecords(patientID);
                    break;
                case 13:
                    // List Patient Records That Are Vaccinated / Not Vaccinated
                    System.out.println("List Patient Records That Are Vaccinated / Not Vaccinated selected.");
                    System.out.print("Enter BCG Vaccination Status (Y/N): ");
                    char isBCGVaccinated = sc.next().charAt(0);
                    patients = patientDAO.getPatientsByBCGVaccinationStatus(isBCGVaccinated);
                    for (Patient p : patients) {
                        System.out.println();
                        patientDAO.printPatientDetails(p);
                    }
                    break;
                case 14:
                    // Add your code for listing patient records that are / are not elevated from close contacts here
                    System.out.println("List Patient Records That Are / Are Not Elevated From Close Contacts selected.");
                    System.out.print("Enter Close Contact Status (Y/N): ");
                    char isElevatedFromCloseContact = sc.next().charAt(0);
                    if (isElevatedFromCloseContact == 'Y') {
                        boolElevatedFromCloseContact = true;
                    } else {
                        boolElevatedFromCloseContact = false;
                    }
                    patients = patientDAO.getPatientsByCloseContactStatus(boolElevatedFromCloseContact);
                    for (Patient p : patients) {
                        System.out.println();
                        patientDAO.printPatientDetails(p);
                    }
                    break;
				case 15:
					exitSubMenu = true;
            }
        }
    }
}