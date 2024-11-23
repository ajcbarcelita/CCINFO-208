package ccinfom_mco;


public class MenuView {

	// options for the main menu
	public static void main_menu() {
		System.out.println("  ");
		System.out.println("  ");
		System.out.println("=======================================================");
		System.out.println("                Application Main Menu			       ");
		System.out.println("-------------------------------------------------------");
		System.out.println("[1] Barangay Health Worker Record Management		   ");
		System.out.println("[2] Prescription Processing							   ");
		System.out.println("[3] Report Generation - Case Productivity 			   ");
		System.out.println("[0] Exit Application								   ");
		System.out.println("=======================================================");
	}
	
	// options for the BHW record management sub-menu
	public static void bhw_rm_menu() {
		System.out.println("  ");
		System.out.println("  ");
		System.out.println("=======================================================");
		System.out.println("        Barangay Health Worker Record Management	   ");
		System.out.println("-------------------------------------------------------");
		System.out.println("[1] Create New Record								   ");
		System.out.println("[2] Update Existing Record 							   ");
		System.out.println("[3] Delete Existing Record				  			   ");
		System.out.println("[4] View Existing Record				  			   ");
		System.out.println("[0] Exit Management									   ");
		System.out.println("=======================================================");
	}
	
	// options for the prescription processing
	public static void prtn_menu() {
		System.out.println("  ");
		System.out.println("  ");
		System.out.println("=======================================================");
		System.out.println("                     Prescriptions                         ");
		System.out.println("-------------------------------------------------------");
		System.out.println("[1] Submit Prescription");
		System.out.println("[2] View Existing Prescription");
		System.out.println("[3] Delete Existing Prescription");
		System.out.println("[0] Exit Prescriptions");
		System.out.println("=======================================================");
	}	
	
	// options for the case productivity report
	public static void cpr_menu() {
		System.out.println("  ");
		System.out.println("  ");
		System.out.println("=======================================================");
		System.out.println("                 Case Productivity Report                   ");
		System.out.println("-------------------------------------------------------");
		System.out.println("Barangay Worker:");
		System.out.println("    [1] Barangay Physician");
		System.out.println("    [2] Barangay Health Worker");
		System.out.println("[0] Exit Report (Enter in BW)");
		System.out.println("=======================================================");
	}
	
	
	public static void month_options() {
	    System.out.println("  ");
	    System.out.println("  ");
	    System.out.println("=======================================================");
	    System.out.println("                      Months                           ");
	    System.out.println("-------------------------------------------------------");
	    System.out.println("[1] January                                            ");
	    System.out.println("[2] February                                           ");
	    System.out.println("[3] March                                              ");
	    System.out.println("[4] April                                              ");
	    System.out.println("[5] May                                                ");
	    System.out.println("[6] June                                               ");
	    System.out.println("[7] July                                               ");
	    System.out.println("[8] August                                             ");
	    System.out.println("[9] September                                          ");
	    System.out.println("[10] October                                           ");
	    System.out.println("[11] November                                          ");
	    System.out.println("[12] December                                          ");
	    System.out.println("=======================================================");
	}
}

