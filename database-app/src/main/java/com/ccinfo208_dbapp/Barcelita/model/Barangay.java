package com.ccinfo208_dbapp.Barcelita.model;

public class Barangay {
    private int barangayID; //PK
    private String barangayName;

    public Barangay(int barangayID, String barangayName) {
        this.barangayID = barangayID;
        this.barangayName = barangayName;
    }

    public Barangay() {

    }

    public int getBarangayID() {
        return barangayID;
    }

    public void setBarangayID(int barangayID) {
        this.barangayID = barangayID;
    }

    public String getBarangayName() {
        return barangayName;
    }

    public void setBarangayName(String barangayName) {
        this.barangayName = barangayName;
    }
}
