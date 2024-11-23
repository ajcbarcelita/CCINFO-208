package com.close_contact_monitoring.model;

import java.time.LocalDate;

public class PatientCase {
	
	private int patientcaseno; //PK
    private int patientID; //PK, and is FK to patient 
    private String case_status; //allowed values: ENUM('Ongoing', 'Cured', 'On Treatment', 'Closed - Died', 'Closed - Lost to Followup', 'Closed - Transferred')
    private LocalDate start_date;
    private int involved_physician; //FK to Physician Table
    private int involved_bhw; //FK to BHW Table
    private LocalDate end_date; //optional field
    private String toBePrescribed; //automatically T at creation
    private LocalDate diagnosis_date; //optional field
    private String finalDiagnosis; //optional field, allowed values: ENUM('DS - TB', 'DR - TB')
    private int transferred_to_medical_center; //optional field, FK to Medical Center Table

    public PatientCase(int patientcaseno, int patientID, String case_status, LocalDate start_date, int involved_physician, int involved_bhw, LocalDate end_date, String toBePrescribed, LocalDate diagnosis_date, String finalDiagnosis, int transferred_to_medical_center) {
        this.patientcaseno = patientcaseno;
        this.patientID = patientID;
        this.case_status = case_status;
        this.start_date = start_date;
        this.involved_physician = involved_physician;
        this.involved_bhw = involved_bhw;
        this.end_date = end_date;
        this.toBePrescribed = toBePrescribed;
        this.diagnosis_date = diagnosis_date;
        this.finalDiagnosis = finalDiagnosis;
        this.transferred_to_medical_center = transferred_to_medical_center;
    }

    public int getPatientcaseno() {
		return patientcaseno;
	}

	public void setPatientcaseno(int patientcaseno) {
		this.patientcaseno = patientcaseno;
	}

	public int getPatientID() {
		return patientID;
	}

	public void setPatientID(int patientID) {
		this.patientID = patientID;
	}

	public String getCase_status() {
		return case_status;
	}

	public void setCase_status(String case_status) {
		this.case_status = case_status;
	}

	public LocalDate getStart_date() {
		return start_date;
	}

	public void setStart_date(LocalDate start_date) {
		this.start_date = start_date;
	}

	public int getInvolved_physician() {
		return involved_physician;
	}

	public void setInvolved_physician(int involved_physician) {
		this.involved_physician = involved_physician;
	}

	public int getInvolved_bhw() {
		return involved_bhw;
	}

	public void setInvolved_bhw(int involved_bhw) {
		this.involved_bhw = involved_bhw;
	}

	public LocalDate getEnd_date() {
		return end_date;
	}

	public void setEnd_date(LocalDate end_date) {
		this.end_date = end_date;
	}

	public String getToBePrescribed() {
		return toBePrescribed;
	}

	public void setToBePrescribed(String toBePrescribed) {
		this.toBePrescribed = toBePrescribed;
	}

	public LocalDate getDiagnosis_date() {
		return diagnosis_date;
	}

	public void setDiagnosis_date(LocalDate diagnosis_date) {
		this.diagnosis_date = diagnosis_date;
	}

	public String getFinalDiagnosis() {
		return finalDiagnosis;
	}

	public void setFinalDiagnosis(String finalDiagnosis) {
		this.finalDiagnosis = finalDiagnosis;
	}

	public int getTransferred_to_medical_center() {
		return transferred_to_medical_center;
	}

	public void setTransferred_to_medical_center(int transferred_to_medical_center) {
		this.transferred_to_medical_center = transferred_to_medical_center;
	}
}
