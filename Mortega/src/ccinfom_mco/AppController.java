package ccinfom_mco;

import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.InputMismatchException;

public class AppController {


    public static void main_controller() {
        int mainAtn = -1;
        Scanner sc = new Scanner(System.in);

        while (mainAtn != 0) {
            MenuView.main_menu();
            System.out.print("Enter selected function: ");

            if (sc.hasNextInt()) {
                mainAtn = sc.nextInt();
                sc.nextLine();  
            } else {
                System.out.println("Invalid input, please enter a number.");
                sc.nextLine(); 
                continue;
            }

            if (mainAtn < 0 || mainAtn > 3) {
                System.out.println("Please enter a valid input between 0 and 3.");
                continue;
            }

            switch (mainAtn) {
                case 1: 
						bhw_rm_controller(sc); 
						break;
                case 2: 
						prtn_controller(sc); 
						break;
                case 3: 
						cpr_controller(sc); 
						break;
                default: 
						System.out.println("\nApplication closed!\n"); 
						break;
            }
        }

        sc.close();
    }



    public static void bhw_rm_controller(Scanner sc) {
        int rmAtn = -1;

        while (rmAtn != 0) {
            MenuView.bhw_rm_menu();
            System.out.print("Enter selected function: ");

            if (sc.hasNextInt()) {
                rmAtn = sc.nextInt();
                sc.nextLine();  
            } else {
                System.out.println("Invalid input, please enter a number.");
                sc.nextLine();  
                continue;
            }

            if (rmAtn < 0 || rmAtn > 4) {
                System.out.println("Please enter a valid input between 0 and 4.");
                continue;
            }

            switch (rmAtn) {
                case 1:
                    if(createRecord(sc) == 1) {
						System.out.println("Record Created!");
						System.out.println("Database Updated!");
					} 
					else {
						System.out.println("No Record Added!");
					}
                    break;
                case 2:
                    if(updateRecord(sc) == 1) {
						System.out.println("Record Updated!");
						System.out.println("Database Updated!");
					}
					else {
						System.out.println("No Record Updated!");
					}
                    break;
                case 3:
                    if (deleteRecord(sc) == 1) {
						System.out.println("Barangay Health Worker Deleted!");
						System.out.println("Database Updated!");
					}
					else {
						System.out.println("No Record Deleted!");
					}
                    break;
                case 4:
                    viewRecord(sc);
                    break;
                default:
                    System.out.println("\nGoing back to Main Menu...\n");
                    break;
            }
        }
    }


	
	//create new record
	public static int createRecord(Scanner sc) {
		String[] bhwData = new String[6];
		Arrays.fill(bhwData, null);  
		bhwData[5] = "-1";
		BarangayHealthWorkerDB bhwdb = new BarangayHealthWorkerDB();
	
		System.out.println("  ");
		System.out.println("  ");
		System.out.println("=======================================================");
		System.out.println("                Create New BHW Record                  ");
		System.out.println("-------------------------------------------------------");
	
		System.out.print("First Name: "); 
		bhwdb.bhw_firstName = sc.next();

		System.out.print("Last Name: "); 
		bhwdb.bhw_lastName = sc.next();

		System.out.print("Middle Name: "); 
		bhwdb.bhw_middleName = sc.next();

		System.out.print("Barangay: ");
		sc.nextLine(); 

		int brgyNo; 
		do {
			bhwdb.barangayAssignedTo = sc.nextLine().trim();
			brgyNo = BarangayHealthWorkerDB.getBrgyNo(bhwdb.barangayAssignedTo);

			if (brgyNo == -1) {
				System.out.println("Barangay does not exist!");
				System.out.print("Barangay: ");
			}
		} while (brgyNo == -1);


	
		System.out.println("=======================================================");
		System.out.println("\n\nProcessing Data...");
	
		return bhwdb.create_bhw_record();
	}
	

	
	//update record
	public static int updateRecord(Scanner sc) {
		String[] bhwData = new String[5];
		Arrays.fill(bhwData, null);
		BarangayHealthWorkerDB bhwdb = new BarangayHealthWorkerDB();


		System.out.println("  ");
		System.out.println("  ");
		System.out.println("=======================================================");
		System.out.println("             Update Existing BHW Record                ");
		System.out.println("-------------------------------------------------------");
		System.out.print("Barangay Health Worker ID: "); 
		int bhwid = sc.nextInt();  // Identifier
		
		//sql check ID, if exist
		if (BarangayHealthWorkerDB.checkBhwId(bhwid) == 1) {
			bhwdb.bhwID = bhwid;

			System.out.println("Type N/A if the field will not be updated\n");
			System.out.print("First Name: "); 
			bhwdb.bhw_firstName = sc.next();

			System.out.print("Last Name: "); 
			bhwdb.bhw_lastName = sc.next();
			
			System.out.print("Middle Name: "); 
			bhwdb.bhw_middleName = sc.next();

			System.out.print("Barangay: "); 
			do {
				bhwData[3] = sc.next();

				if (bhwData[3] != "N/A") {
					break;
				}

				if (BarangayHealthWorkerDB.getBrgyNo(bhwData[3]) == -1) {
					System.out.println("Barangay does not exist!");
					System.out.print("Barangay: "); 
				}
			}
			while (BarangayHealthWorkerDB.getBrgyNo(bhwData[3]) == -1);

			
			bhwdb.barangayAssignedTo = bhwData[3]; 
			

			System.out.println("=======================================================");
			System.out.println("Updating Database...");
			return bhwdb.update_bhw_record();
		}
		else {
			System.out.println("=======================================================");
			System.out.println("Barangay Health Worker ID does not exist!");
			return 0;
		}  
	}
	
	

	//delete record
	public static int deleteRecord(Scanner sc) {
		int bhwID = 0;
		BarangayHealthWorkerDB bhwdb = new BarangayHealthWorkerDB();
	
		System.out.println("  ");
		System.out.println("  ");
		System.out.println("=======================================================");
		System.out.println("              Delete Existing BHW Record               ");
		System.out.println("-------------------------------------------------------");
		System.out.print("Barangay Health Worker ID: ");
		bhwID = sc.nextInt(); 
		if (BarangayHealthWorkerDB.checkBhwId(bhwID) == 1) {
			bhwdb.bhwID = bhwID;
			System.out.println("=======================================================");
			System.out.println("Processing Data...");

			return bhwdb.delete_bhw_record();
		}
		else {
			System.out.println("=======================================================");
			System.out.println("Barangay Health Worker ID does not exist!");
			System.out.println("Record Deletion Unsuccessful!");
			return 0;
		}
	}
	


	//view record
	public static void viewRecord(Scanner sc) {
		int bhwID = 0;
		BarangayHealthWorkerDB bhwdb = new BarangayHealthWorkerDB();
	
		System.out.println("  ");
		System.out.println("  ");
		System.out.println("=======================================================");
		System.out.println("              View Existing BHW Record                 ");
		System.out.println("-------------------------------------------------------");
		System.out.print("Barangay Health Worker ID: ");
		bhwID = sc.nextInt();  
		if (BarangayHealthWorkerDB.checkBhwId(bhwID) == 1) {
			bhwdb.bhwID = bhwID;
			System.out.println("=======================================================");
			System.out.println("Processing Data...");
			BHW bhw = bhwdb.view_bhw_record(); 
			bhw.printBHWRecord();
			System.out.println("=======================================================");
		}
		else {
			System.out.println("=======================================================");
			System.out.println("Barangay Health Worker ID does not exist!");
			System.out.println("Record Viewing Unsuccessful!");
		}
	}

		
	
	//prescription controller
	public static void prtn_controller(Scanner sc) {
		int prtnAct = -1;
	
		while (prtnAct != 0) {
			MenuView.prtn_menu();
			System.out.print("Enter selected function: ");
	
			if (sc.hasNextInt()) {
				prtnAct = sc.nextInt();
				sc.nextLine();  
			} else {
				System.out.println("Invalid input, please enter a number.");
				sc.nextLine();  
				continue;
			}
	
			if (prtnAct < 0 || prtnAct > 3) {
				System.out.println("Please enter a valid input between 0 and 3.");
				continue;
			}
	
			switch (prtnAct) {
				case 1:
					if (submitPrescription(sc) == 1) {
						System.out.println("Prescription Recorded!");
						System.out.println("Database Updated!");
					}
					break;
				case 2:
					viewPrescription(sc);
					break;
				case 3:
					if (deletePrescription(sc) == 1) {
						System.out.println("Prescription Deleted!");
						System.out.println("Database Updated!");
					}
					break;
				default:
					System.out.println("Going back to Main Menu...");
					break;
			}
		}
	}

	
	
	public static int submitPrescription(Scanner sc) {
		int nMed = 0; 
		PrescriptionsDB pdb = new PrescriptionsDB();
	
		System.out.println("  ");
		System.out.println("  ");
		System.out.println("=======================================================");
		System.out.println("                Submit Prescription                    ");
		System.out.println("-------------------------------------------------------");
		System.out.print("Patient Case ID:"); 
		int caseno = sc.nextInt(); 

		if (PrescriptionsDB.checkCaseNo(caseno) == 1 && PrescriptionsDB.toBePrescribed(caseno) == 1 ) {

			pdb.patientCaseID = caseno;

			System.out.print("Prescription Serial Number:"); 
			do {
				pdb.prescriptionSerialNumber = sc.next(); 

				if (pdb.prescriptionSerialNumber.length() != 12) {
					System.out.println("Please enter a valid Prescription Serial Number (12 Digits)!");
					System.out.print("Prescription Serial Number:");  
				}
			}
			while (pdb.prescriptionSerialNumber.length() != 12);
		
			System.out.print("Number of Prescriptions:"); 
			nMed = sc.nextInt(); 

			while(nMed < 0) {
				System.out.println("Please enter an integer greater than 0\n");
				System.out.print("Number of Prescriptions:");
				nMed = sc.nextInt(); 
			}
		
			// Initialize data structures
			String[] medData = new String[nMed];       // Medicine Names (Generic Names)
			double[] dosage = new double[nMed];        // Dosage (mg/kg)
			int[][] qdf_ds = new int[nMed][2];         // Frequency and Duration (per day, in weeks)

			// Fill default values
			Arrays.fill(medData, null);
			Arrays.fill(dosage, 0.0);
			for (int i = 0; i < nMed; i++) {
				Arrays.fill(qdf_ds[i], -1);            // Initialize frequency and duration to invalid values
			}

			// Medicine input loop
			for (int i = 0; i < nMed; i++) {
				int medNumber = i + 1;  // Dynamic numbering starts from 1
				System.out.println("----------------------------------------------------");
				System.out.println("Input details for Medicine #" + medNumber);
				
				// Get valid medicine name
				while (true) {
					System.out.print("Medicine Name: ");
					medData[i] = sc.next();
					if (PrescriptionsDB.checkMedicine(medData[i]) == 0) {
						System.out.println("Invalid medicine name. Please input a valid generic name.");
					} else {
						break;
					}
				}
				pdb.prescriptionNames.add(medData[i]);  // Add valid medicine name to the list

				// Get valid dosage, frequency, and duration
				while (true) {
					try {
						System.out.print("Dosage (mg/kg): ");
						dosage[i] = sc.nextDouble();

						System.out.print("Frequency (times per day): ");
						qdf_ds[i][0] = sc.nextInt();

						System.out.print("Duration (in weeks): ");
						qdf_ds[i][1] = sc.nextInt();

						// Validate inputs
						if (dosage[i] <= 0 || qdf_ds[i][0] <= 0 || qdf_ds[i][1] <= 0) {
							System.out.println("All values must be positive numbers. Please try again.");
						} else {
							break;
						}
					} catch (InputMismatchException e) {
						System.out.println("Invalid input. Please enter numeric values.");
						sc.nextLine();  // Clear the invalid input
					}
				}

				// Add valid data to PrescriptionDB
				pdb.dosage.add(dosage[i]);
				pdb.frequency.add(qdf_ds[i][0]);
				pdb.duration.add(qdf_ds[i][1]);
			}

			// Handle date parsing and setting
			try {
				System.out.print("Prescription Issue Date (dd-mm-yyyy):"); 
				String dateInput = sc.next();  // Read date input from user
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				Date parsedDate = dateFormat.parse(dateInput);

				java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());
				pdb.date = sqlDate;
				System.out.println("Date successfully parsed and added: " + sqlDate);
			} catch (ParseException e) {
				System.out.println("Failed to parse date: " + e.getMessage());
			}


			System.out.println("=======================================================");
			System.out.println("Processing Rx...");

			return pdb.submit_prescriptions();
	} else {
		System.out.println("=======================================================");
		if (PrescriptionsDB.checkCaseNo(caseno) == 0) {
			System.out.println("Patient Case Number does not exist!");
		}
		else {
			System.out.println("Patient cannot be prescribed!");
		}
		return 0;
	}

	}
	

	
	public static void viewPrescription(Scanner sc) {
    PrescriptionsDB pdb = new PrescriptionsDB();
    int caseno = 0;
    String prtnSerialNo = "";

    System.out.println("\n\n=======================================================");
    System.out.println("            View Existing Prescription               ");
    System.out.println("-------------------------------------------------------");
    
    // Get the Patient Case Number
    System.out.print("Patient Case Number: "); 
    caseno = sc.nextInt();  // Patient case number as identifier
    
    // Check if case number exists
    if (PrescriptionsDB.checkCaseNo(caseno) == 1) {
        pdb.patientCaseID = caseno;

        // Get the Prescription Serial Number, ensuring it is valid
        System.out.print("Prescription Serial Number: "); 
        prtnSerialNo = sc.next(); 
        while (prtnSerialNo.length() < 12) {
            System.out.println("Please enter a valid serial number (12 characters minimum).");
            System.out.print("Prescription Serial Number: "); 
            prtnSerialNo = sc.next(); 
        }

        pdb.prescriptionSerialNumber = prtnSerialNo;

        // Process the prescription data
        System.out.println("=======================================================");
        System.out.println("Processing Data...");

        pdb.view_prescriptions();  // Assuming this method prints the prescription details
    } else {
        System.out.println("=======================================================");
        System.out.println("Patient Case Number does not exist!");
    }
}

	

	public static int deletePrescription(Scanner sc) {
		PrescriptionsDB pdb = new PrescriptionsDB();
		int caseno = 0;
		String prtnSerialNo = "";
	
		System.out.println("  ");
		System.out.println("  ");
		System.out.println("=======================================================");
		System.out.println("            Delete Existing Prescription               ");
		System.out.println("-------------------------------------------------------");
		System.out.print("Patient Case Number:"); 
		caseno = sc.nextInt();  // Identifier
	
		if (PrescriptionsDB.checkCaseNo(caseno) == 1) {
			pdb.patientCaseID = caseno;
	
			System.out.print("Prescription Serial Number:"); 
			prtnSerialNo = sc.next();  // Get the serial number input
	
			// Validate the serial number length
			while (prtnSerialNo.length() < 12) {
				System.out.println("Please enter a valid serial number!");
				System.out.print("Prescription Serial Number:"); 
				prtnSerialNo = sc.next(); // Prompt again if invalid length
			}
	
			pdb.prescriptionSerialNumber = prtnSerialNo;
	
			System.out.println("=======================================================");
			System.out.println("Processing Data...");
	
			return pdb.delete_prescriptions();
		}
		else {
			System.out.println("=======================================================");
			System.out.println("Patient Case Number does not exist!");
			return 0;
		}
	}
	
	

	public static void cpr_controller(Scanner sc) {
		int worker = -1;
		int month = -1;
		int year = -1;

		Report rp = new Report();

		System.out.println("=======================================================");
		System.out.println("			Year");
		System.out.println("-------------------------------------------------------");
		System.out.println("2023 -> 2024");
		System.out.println("=======================================================");

		do {
			System.out.print("Year: ");
			year = sc.nextInt();
		
			if (year < 2023 || year > 2024) {
				System.out.println("Please enter a valid year!");
			}
		} while (year < 2023 || year > 2024);
		
		MenuView.month_options();
		do {
			System.out.print("Month (1-12): ");
			month = sc.nextInt();
		
			if (month < 1 || month > 12) {
				System.out.println("Please enter a valid month number!");
			}
		} while (month < 1 || month > 12);

		rp.month = month;
		rp.year = year;

		MenuView.cpr_menu();
		while (worker < 0 || worker > 2) {
			System.out.print("Barangay Worker: ");
			worker = sc.nextInt();
		}

		if (worker != 0) {
			if (worker == 1) {
				System.out.print("Physician PRC License Number: ");
				int prcno = sc.nextInt();

				if (PrescriptionsDB.checkPhyID(prcno) == 1) {
					rp.physicianPRCNo = prcno;

					int pcNum = rp.physician_cp_report();
					if (pcNum >= 0) {
						System.out.println("Number of Patient Case Handled: " + pcNum);
					}
				}
				else {
					System.out.println("Physician PRC License Number does not exists!");
				}
			}
			else {
				System.out.print("BHW ID Number: ");
				rp.bhwID = sc.nextInt();

				int pcNum = rp.bhw_cp_report();
				if (pcNum >= 0) {
					System.out.println("Number of Patient Case Handled: " + pcNum);
				}
			}
		}
		else {
			System.out.println("Exiting Case Report...");
		}
	}
		



}

