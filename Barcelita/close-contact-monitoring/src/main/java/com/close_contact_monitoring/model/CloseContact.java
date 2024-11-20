package com.close_contact_monitoring.model;

import java.time.LocalDate;

public class CloseContact {
    private int closeContactID; //PK
    private String lastname;
    private String firstname;
    private String middlename;
    private LocalDate birthdate;
    private char sex; //allowed values: ENUM('M', 'F')
    private char is_elevated_to_patient; //allowed values: ENUM('Y', 'N')

    public CloseContact(int closeContactID, String lastname, String firstname, String middlename, LocalDate birthdate, char sex, char is_elevated_to_patient) {
        this.closeContactID = closeContactID;
        this.lastname = lastname;
        this.firstname = firstname;
        this.middlename = middlename;
        this.birthdate = birthdate;
        this.sex = sex;
        this.is_elevated_to_patient = is_elevated_to_patient;
    }

    public int getCloseContactID() {
        return closeContactID;
    }

    public void setCloseContactID(int closeContactID) {
        this.closeContactID = closeContactID;
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

    public char getIs_elevated_to_patient() {
        return is_elevated_to_patient;
    }

    public void setIs_elevated_to_patient(char is_elevated_to_patient) {
        this.is_elevated_to_patient = is_elevated_to_patient;
    }
}
