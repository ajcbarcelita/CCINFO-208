package com.ccinfo208_dbapp.Barcelita.model;

import java.time.LocalDate;

public class Guardian {
    private int guardianID; //PK
    private String lastname;
    private String firstname;
    private String middlename;
    private LocalDate birthdate;
    private char sex; //allowed values: ENUM('M', 'F')
    private String contactno;
    private String email;
    private String relationship_to_patient; //allowed values: ENUM('Father', 'Mother', 'Legal Guardian', 'Relative', 'Other')
    private char is_emergency_contact; // allowed values: ENUM('Y', 'N')

    public Guardian(int guardianID, String lastname, String firstname, String middlename, LocalDate birthdate, char sex, String contactno, String email, String relationship_to_patient, char is_emergency_contact) {
        this.guardianID = guardianID;
        this.lastname = lastname;
        this.firstname = firstname;
        this.middlename = middlename;
        this.birthdate = birthdate;
        this.sex = sex;
        this.contactno = contactno;
        this.email = email;
        this.relationship_to_patient = relationship_to_patient;
        this.is_emergency_contact = is_emergency_contact;
    }
    
    public int getGuardianID() {
		return guardianID;
	}

	public void setGuardianID(int guardianID) {
		this.guardianID = guardianID;
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

	public String getContactno() {
		return contactno;
	}

	public void setContactno(String contactno) {
		this.contactno = contactno;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRelationship_to_patient() {
		return relationship_to_patient;
	}

	public void setRelationship_to_patient(String relationship_to_patient) {
		this.relationship_to_patient = relationship_to_patient;
	}

	public char getIs_emergency_contact() {
		return is_emergency_contact;
	}

	public void setIs_emergency_contact(char is_emergency_contact) {
		this.is_emergency_contact = is_emergency_contact;
	}
}
