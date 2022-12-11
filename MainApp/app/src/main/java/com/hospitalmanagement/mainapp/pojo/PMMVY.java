package com.hospitalmanagement.mainapp.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

public class PMMVY {

    @SerializedName("registrationDate")
    @Expose
    private String registrationDate;
    @SerializedName("sixmonthVisit")
    @Expose
    private String sixmonthVisit;
    @SerializedName("Penta3date")
    @Expose
    private String penta3date;

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
        return penta3date;
    }

    public void setPenta3date(String penta3date) {
        this.penta3date = penta3date;
    }

}