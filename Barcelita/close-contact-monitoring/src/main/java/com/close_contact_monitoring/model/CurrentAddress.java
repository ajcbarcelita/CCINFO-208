package com.close_contact_monitoring.model;

public class CurrentAddress {
    private int addressID; //PK
    private String ca_houseno;
    private String ca_subdivision; //optional field
    private String ca_street;
    private int ca_barangay; //FK to Barangay Table

    public CurrentAddress(int addressID, String ca_houseno, String ca_subdivision, String ca_street, int ca_barangay) {
        this.addressID = addressID;
        this.ca_houseno = ca_houseno;
        this.ca_subdivision = ca_subdivision;
        this.ca_street = ca_street;
        this.ca_barangay = ca_barangay;
    }

    public int getAddressID() {
        return addressID;
    }

    public void setAddressID(int addressID) {
        this.addressID = addressID;
    }

    public String getCa_houseno() {
        return ca_houseno;
    }

    public void setCa_houseno(String ca_houseno) {
        this.ca_houseno = ca_houseno;
    }

    public String getCa_subdivision() {
        return ca_subdivision;
    }

    public void setCa_subdivision(String ca_subdivision) {
        this.ca_subdivision = ca_subdivision;
    }

    public String getCa_street() {
        return ca_street;
    }

    public void setCa_street(String ca_street) {
        this.ca_street = ca_street;
    }

    public int getCa_barangay() {
        return ca_barangay;
    }

    public void setCa_barangay(int ca_barangay) {
        this.ca_barangay = ca_barangay;
    }
}
