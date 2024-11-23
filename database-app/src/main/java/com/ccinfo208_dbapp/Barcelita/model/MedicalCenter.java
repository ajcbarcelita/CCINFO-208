package com.ccinfo208_dbapp.Barcelita.model;

public class MedicalCenter {
    private int medicalcenterID; //PK
    private String name;
    private int located_at_barangay; //FK to Barangay
    private String landline_no;
    private String mobile_no;
    private String email;

    public MedicalCenter(int medicalcenterID, String name, int located_at_barangay, String landline_no, String mobile_no, String email) {
        this.medicalcenterID = medicalcenterID;
        this.name = name;
        this.located_at_barangay = located_at_barangay;
        this.landline_no = landline_no;
        this.mobile_no = mobile_no;
        this.email = email;
    }

    public int getMedicalcenterID() {
        return medicalcenterID;
    }

    public void setMedicalcenterID(int medicalcenterID) {
        this.medicalcenterID = medicalcenterID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLocated_at_barangay() {
        return located_at_barangay;
    }

    public void setLocated_at_barangay(int located_at_barangay) {
        this.located_at_barangay = located_at_barangay;
    }

    public String getLandline_no() {
        return landline_no;
    }

    public void setLandline_no(String landline_no) {
        this.landline_no = landline_no;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
