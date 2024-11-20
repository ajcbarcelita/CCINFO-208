package com.close_contact_monitoring.model;

public class PatientGuardian {
    private int patientID; //PK, FK to Patient Table
    private int guardianID; //PK, FK to Guardian Table

    public PatientGuardian(int patientID, int guardianID) {
        this.patientID = patientID;
        this.guardianID = guardianID;
    }

    public int getPatientID() {
        return patientID;
    }

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public int getGuardianID() {
        return guardianID;
    }

    public void setGuardianID(int guardianID) {
        this.guardianID = guardianID;
    }
}
