package com.close_contact_monitoring.model;

public class Physician {
    private int physician_prc_id; //PK, can be 7-8 digits
    private String lastname;
    private String firstname;
    private String middlename;
    private int barangay_assignedto; //FK to Barangay

    public Physician(int physician_prc_id, String lastname, String firstname, String middlename, int barangay_assignedto) {
        this.physician_prc_id = physician_prc_id;
        this.lastname = lastname;
        this.firstname = firstname;
        this.middlename = middlename;
        this.barangay_assignedto = barangay_assignedto;
    }

    public int getPhysician_prc_id() {
        return physician_prc_id;
    }

    public void setPhysician_prc_id(int physician_prc_id) {
        this.physician_prc_id = physician_prc_id;
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
