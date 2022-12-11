package com.hospitalmanagement.mainapp.GetterSetter;

public class PMMVY {
    private String registrationDate;
    private String sixmonthVisit;
    private String Penta3date;

    public PMMVY() {

    }

    public PMMVY(String registrationDate, String sixmonthVisit, String penta3date) {
        this.registrationDate = registrationDate;
        this.sixmonthVisit = sixmonthVisit;
        Penta3date = penta3date;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getSixmonthVisit() {
        return sixmonthVisit;
    }

    public void setSixmonthVisit(String sixmonthVisit) {
        this.sixmonthVisit = sixmonthVisit;
    }

    public String getPenta3date() {
        return Penta3date;
    }

    public void setPenta3date(String penta3date) {
        Penta3date = penta3date;
    }
}
