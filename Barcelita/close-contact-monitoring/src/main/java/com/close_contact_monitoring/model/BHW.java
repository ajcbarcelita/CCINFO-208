package com.close_contact_monitoring.model;

public class BHW {
    private int bhwID; //PK
    private String lastname;
    private String firstname;
    private String middlename;
    private int barangay_assignedto; //FK to Barangay

    public BHW(int bhwID, String lastname, String firstname, String middlename, int barangay_assignedto) {
        this.bhwID = bhwID;
        this.lastname = lastname;
        this.firstname = firstname;
        this.middlename = middlename;
        this.barangay_assignedto = barangay_assignedto;
    }

    public int getbhwID() {
        return bhwID;
    }

    public void setbhwID(int bhwID) {
        this.bhwID = bhwID;
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

    public int getBarangay_assignedto() {
        return barangay_assignedto;
    }

    public void setBarangay_assignedto(int barangay_assignedto) {
        this.barangay_assignedto = barangay_assignedto;
    }
}
