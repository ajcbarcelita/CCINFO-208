package com.ccinfo208_dbapp.Barcelita.model;

public class Comorbidity {
    private int comorbidityID; //PK
    private String comorbidityName;

    public Comorbidity() {

    }
    
    public Comorbidity(int comorbidityID, String comorbidityName) {
        this.comorbidityID = comorbidityID;
        this.comorbidityName = comorbidityName;
    }

    public int getComorbidityID() {
        return comorbidityID;
    }

    public void setComorbidityID(int comorbidityID) {
        this.comorbidityID = comorbidityID;
    }

    public String getComorbidityName() {
        return comorbidityName;
    }

    public void setComorbidityName(String comorbidityName) {
        this.comorbidityName = comorbidityName;
    }
}
