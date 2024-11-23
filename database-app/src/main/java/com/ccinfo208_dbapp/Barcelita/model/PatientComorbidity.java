package com.ccinfo208_dbapp.Barcelita.model;

/**
 * 	A model class for the PatientComorbidity table in the MySQL database.
 * 	Contains the attributes of the PatientComorbidity table and their respective getter and setter methods.
 *  Serves as a more organized way to handle the data of the PatientComorbidity table, which is a junction table for Patient and Ref_Comorbidity tables.
 */

public class PatientComorbidity {
    private int patientID;
    private int comorbidityID;

    public PatientComorbidity(int patientID, int comorbidityID) {
        this.patientID = patientID;
        this.comorbidityID = comorbidityID;
    }

    public int getPatientID() {
        return patientID;
    }

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public int getComorbidityID() {
        return comorbidityID;
    }

    public void setComorbidityID(int comorbidityID) {
        this.comorbidityID = comorbidityID;
    }
}
