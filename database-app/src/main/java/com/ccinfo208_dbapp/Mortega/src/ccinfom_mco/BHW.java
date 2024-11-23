package com.ccinfo208_dbapp.Mortega.src.ccinfom_mco;

public class BHW {
    private int bhwID;
    private String lastname;
    private String firstname;
    private String middlename;
    private int barangayAssignedTo;

    public int getBhwID() {
        return bhwID;
    }
    public void setBhwID(int bhwID) {
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

    public int getBarangayAssignedTo() {
        return barangayAssignedTo;
    }
    public void setBarangayAssignedTo(int barangayAssignedTo) {
        this.barangayAssignedTo = barangayAssignedTo;
    }

    public void printBHWRecord() {
        System.out.println("BHW Record:");
        System.out.println("BHW ID: " + bhwID);
        System.out.println("Name: " + lastname + ", " + firstname + " " + middlename);
        System.out.println("Barangay Assigned To: " + BarangayHealthWorkerDB.getBrgyName(barangayAssignedTo));
    }
}


