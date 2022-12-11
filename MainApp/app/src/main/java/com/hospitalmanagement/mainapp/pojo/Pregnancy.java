package com.hospitalmanagement.mainapp.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

public class Pregnancy {

    @SerializedName("pregnancyId")
    @Expose
    private String pregnancyId;
    @SerializedName("para")
    @Expose
    private Integer para;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("complicationPreviousPregnancy")
    @Expose
    private String complicationPreviousPregnancy;
    @SerializedName("lastMenstrualDate")
    @Expose
    private String lastMenstrualDate;
    @SerializedName("expectedDateDelivery")
    @Expose
    private String expectedDateDelivery;
    @SerializedName("expectedPlaceDelivery")
    @Expose
    private String expectedPlaceDelivery;
    @SerializedName("registrationDate")
    @Expose
    private String registrationDate;
    @SerializedName("delivery")
    @Expose
    private Delivery delivery;
    @SerializedName("PMMVY")
    @Expose
    private PMMVY pMMVY;
    @SerializedName("JSY")
    @Expose
    private JSY jSY;

    public Integer getPara() {
        return para;
    }

    public void setPara(Integer para) {
        this.para = para;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComplicationPreviousPregnancy() {
        return complicationPreviousPregnancy;
    }

    public void setComplicationPreviousPregnancy(String complicationPreviousPregnancy) {
        this.complicationPreviousPregnancy = complicationPreviousPregnancy;
    }

    public String getLastMenstrualDate() {
        return lastMenstrualDate;
    }

    public void setLastMenstrualDate(String lastMenstrualDate) {
        this.lastMenstrualDate = lastMenstrualDate;
    }

    public String getExpectedDateDelivery() {
        return expectedDateDelivery;
    }

    public void setExpectedDateDelivery(String expectedDateDelivery) {
        this.expectedDateDelivery = expectedDateDelivery;
    }

    public String getExpectedPlaceDelivery() {
        return expectedPlaceDelivery;
    }

    public void setExpectedPlaceDelivery(String expectedPlaceDelivery) {
        this.expectedPlaceDelivery = expectedPlaceDelivery;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public PMMVY getPMMVY() {
        return pMMVY;
    }

    public void setPMMVY(PMMVY pMMVY) {
        this.pMMVY = pMMVY;
    }

    public JSY getJSY() {
        return jSY;
    }

    public void setJSY(JSY jSY) {
        this.jSY = jSY;
    }

    public String getPregnancyId() {
        return pregnancyId;
    }

    public void setPregnancyId(String pregnancyId) {
        this.pregnancyId = pregnancyId;
    }
}