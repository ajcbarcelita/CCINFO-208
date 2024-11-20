package ccinfom_mco;

import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;

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
			//the barangay name will not be accepted unless it exists in the database
			do {
				bhwdb.barangayAssignedTo = sc.next();

				if (BarangayHealthWorkerDB.getBrgyNo(bhwdb.barangayAssignedTo) == -1) {
					System.out.println("Barangay does not exist!");
					System.out.print("Barangay: "); 
				}
			}
			while (BarangayHealthWorkerDB.getBrgyNo(bhwdb.barangayAssignedTo) == -1);
	
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

			System.out.println("Type N/A if the field will not be updated\n");
			System.out.print("First Name: "); 
			bhwData[0] = sc.next();
			if (bhwData[0] != "N/A") {
				bhwdb.bhw_firstName = bhwData[1];
			}

			System.out.print("Last Name: "); 
			bhwData[1] = sc.next();
			if (bhwData[1] != "N/A") {
				bhwdb.bhw_lastName = bhwData[1];
			}
			
			System.out.print("Middle Name: "); 
			bhwData[2] = sc.next();
			if (bhwData[2] != "N/A") {
				bhwdb.bhw_middleName = bhwData[2];
			}

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

			if (bhwData[3] != "N/A") {
				bhwdb.barangayAssignedTo = bhwData[4]; 
			}


			// Validate Activeness Status
			while (true) {
				System.out.print("Activeness Status (A - Active, N - Inactive): ");
				bhwData[4] = sc.next();
				
				if (bhwData[4].equals("A") || bhwData[4].equals("N") || bhwData[4].equals("N/A")) {
					bhwdb.bhw_isActive = bhwData[5];
					break;  
				} else {
					System.out.println("Please enter a valid input! ('A', 'N', or 'N/A')");
				}
			}
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
			System.out.print("Physician PRC License Number:"); 
			int prcno = sc.nextInt(); 

		if (PrescriptionsDB.checkPhyID(prcno, caseno) == 1) {
			pdb.patientCaseID = caseno;
			pdb.physicianPRCLicNumber = prcno;

			System.out.print("Prescription Serial Number:"); 
			do {
				pdb.prescriptionSerialNumber = sc.next(); 

				if (pdb.prescriptionSerialNumber.length() != 12) {
					System.out.println("Please enter a valid Prescription Serial Number (12 Digits)!");
					System.out.print("Prescription Serial Number:");  
				}
			}
			while (pdb.prescriptionSerialNumber.length() != 12);

			System.out.print("Prescription Issue Date (dd-mm-yyyy):"); 
			String date_issue = sc.next();

			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				Date parsedDate = dateFormat.parse(date_issue);

				java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());

				pdb.date = sqlDate;
			} catch (ParseException e) {
				System.out.println("Failed to parse date: " + e.getMessage());
			}
		
			System.out.print("Number of Prescriptions:"); 
			while(nMed < 0) {
				System.out.println("Please enter an integer greater than 0\n");
				System.out.print("Number of Prescriptions:");
				nMed = sc.nextInt(); 
			}
		
			String[] medData = new String[nMed];    // Medicine Name (Generic Name)
			int[][] qdf_ds = new int[nMed][3];      // Medicine Quantity, Dosage, Frequency
			double[] dosage = new double[nMed];
			Arrays.fill(medData, null);
			Arrays.fill(dosage, 0.0);
		
			for (int i = 0; i < nMed; i++) {  		// Initialization
				Arrays.fill(qdf_ds[i], -1);
			}
		
			for(int i = 0; i < nMed; i++) {
				int y = i + 1;
				System.out.print("Medicine # " + y + " Name: ");  
				do {
					medData[i] = sc.next();

					if (PrescriptionsDB.checkMedicine(medData[i]) == 0) {
						System.out.println("Please input a valid generic medicine name!");
						System.out.print("Medicine # " + y + " Name: ");
					}
				} 
				while(PrescriptionsDB.checkMedicine(medData[i]) == 0); //medicine does not exist


				pdb.prescriptionNames.add(medData[i]);
		
				while(qdf_ds[i][0] < 0 || qdf_ds[i][1] < 0 || qdf_ds[i][2] < 0) { 
					System.out.print("Medicine # " + y + "Dosage (mg/mL): ");  
					dosage[i] = sc.nextInt();

					System.out.print("Medicine # " + y + " Frequency (Per Day): ");  
					qdf_ds[i][1] = sc.nextInt();

					System.out.print("Medicine # " + y + " Intake Duration: ");  
					qdf_ds[i][2] = sc.nextInt();
		
					if (qdf_ds[i][0] < 0 || qdf_ds[i][1] < 0 || qdf_ds[i][2] < 0) {
						System.out.println("Please enter non-negative values for Dosage, Frequency, and Intake Duration.");
					}
				}
				pdb.dosage.add(dosage[i]);
				pdb.frequency.add(qdf_ds[i][1]);
				pdb.frequency.add(qdf_ds[i][2]);
			}

			System.out.println("=======================================================");
			System.out.println("Processing Rx...");

			return pdb.submit_prescriptions();
		}
		else {
			System.out.println("=======================================================");
			System.out.println("Please ensure that the numbers provided are valid and correct!");
			return 0;
		}
	} else {
		System.out.println("=======================================================");
		System.out.println("Patient Case Number does not exist!");
		return 0;
	}

	}
	

	
	public static void viewPrescription(Scanner sc) {
		PrescriptionsDB pdb = new PrescriptionsDB();
		int caseno = 0;
		String prtnSerialNo = "";

		System.out.println("  ");
		System.out.println("  ");
		System.out.println("=======================================================");
		System.out.println("            View Existing Prescription               ");
		System.out.println("-------------------------------------------------------");
		System.out.print("Patient Case Number:"); 
		caseno = sc.nextInt();  // Identifier

		if (PrescriptionsDB.checkCaseNo(caseno) == 1) {
			pdb.patientCaseID = caseno;
			
			System.out.print("Prescription Serial Number:"); 
			prtnSerialNo = sc.next(); 
			do {
				prtnSerialNo = sc.next(); 
			}
			while (prtnSerialNo.length() < 12);

			pdb.prescriptionSerialNumber = prtnSerialNo;
			
			System.out.println("=======================================================");
			System.out.println("Processing Data...");

			pdb.view_prescriptions();
		}
		else {
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
			prtnSerialNo = sc.next(); 
			do {
				prtnSerialNo = sc.next(); 
			}
			while (prtnSerialNo.length() < 12);

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
						System.out.println("Number of Patient Case Handled: ");
					}
				}
				else {
					System.out.println("Physician PRC License Number does not exists!");
				}
			}
			else {
				System.out.print("BHW First Name: ");
				rp.bhw_firstName = sc.next();

				System.out.print("BHW Last Name: ");
				rp.bhw_lastName = sc.next();

				System.out.print("BHW Middle Name: ");
				rp.bhw_middleName = sc.next();

				System.out.print("BHW Barangay: ");
				rp.bhw_firstName = sc.next();

				int pcNum = rp.bhw_cp_report();
				if (pcNum >= 0) {
					System.out.println("Number of Patient Case Handled: ");
				}
			}
		}
		else {
			System.out.println("Exiting Case Report...");
		}
	}
		



	public static void main(String[] args) {
		main_controller();
	}

}

