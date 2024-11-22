package com.close_contact_monitoring.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Patient {
	private int patientID; //PK
	private String lastname;
	private String firstname;
	private String middlename;
	private LocalDate birthdate;
	private char sex;
	private char is_bcg_vaccinated;
	private int addressID; //FK to Address Table
	private int closecontactcaseID; //optional FK to patient's close contact case if ever he came from being a close contact

	//arraylist of comorbidities and guardians, because a Patient may or may not have comorbidities, and may have 1 or more guardians
	private ArrayList<PatientComorbidity> comorbidities;
	private ArrayList<PatientGuardian> guardians;
	
	public Patient() {
		
	}

	public Patient(int patientID, String lastname, String firstname, String middlename, LocalDate birthdate, char sex, char is_bcg_vaccinated, int addressID, int closeContactCaseID) {
        this.patientID = patientID;
        this.lastname = lastname;
        this.firstname = firstname;
        this.middlename = middlename;
        this.birthdate = birthdate;
        this.sex = sex;
        this.is_bcg_vaccinated = is_bcg_vaccinated;
        this.addressID = addressID;
        this.closecontactcaseID = closeContactCaseID;
    }
	
	public int getPatientID() {
		return patientID;
	}

	public void setPatientID(int patientID) {
		this.patientID = patientID;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getMiddlename() {
		return middlename;
	}

	public void setMiddlename(String middlename) {
		this.middlename = middlename;
	}

	public LocalDate getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(LocalDate birthdate) {
		this.birthdate = birthdate;
	}

	public char getSex() {
		return sex;
	}

	public void setSex(char sex) {
		this.sex = sex;
	}

	public char getIs_bcg_vaccinated() {
		return is_bcg_vaccinated;
	}

	public void setIs_bcg_vaccinated(char is_bcg_vaccinated) {
		this.is_bcg_vaccinated = is_bcg_vaccinated;
	}

	public int getAddressID() {
		return addressID;
	}

	public void setAddressID(int addressID) {
		this.addressID = addressID;
	}

	public int getClosecontactcaseID() {
		return closecontactcaseID;
	}

	public void setClosecontactcaseID(int closecontactcaseID) {
		this.closecontactcaseID = closecontactcaseID;
	}

	public ArrayList<PatientComorbidity> getComorbidities() {
		return comorbidities;
	}

	public void setComorbidities(ArrayList<PatientComorbidity> comorbidities) {
		this.comorbidities = comorbidities;
	}

	public ArrayList<PatientGuardian> getGuardians() {
		return guardians;
	}

	public void setGuardians(ArrayList<PatientGuardian> guardians) {
		this.guardians = guardians;
	}
}
