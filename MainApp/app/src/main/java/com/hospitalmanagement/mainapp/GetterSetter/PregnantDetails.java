package com.hospitalmanagement.mainapp.GetterSetter;

public class PregnantDetails {
    private String pregnancyId;
    private int para;
    private String name;
    private String momObjectID;
    private String complicationPreviousPregnancy;
    private String lastMenstrualDate;
    private String expectedDateDelivery;
    private String expectedPlaceDelivery;
    private com.hospitalmanagement.mainapp.GetterSetter.JSY JSY;
    private PMMVY PMMVY;
    private Delivery delivery;

    public PregnantDetails(String pregnancyId, int para, String name, String motherId, String complicationPreviousPregnancy, String lastMenstrualDate, String expectedDateDelivery, String expectedPlaceDelivery, com.hospitalmanagement.mainapp.GetterSetter.JSY JSY, com.hospitalmanagement.mainapp.GetterSetter.PMMVY PMMVY, Delivery delivery) {
        this.pregnancyId = pregnancyId;
        this.para = para;
        this.name = name;
        this.momObjectID = motherId;
        this.complicationPreviousPregnancy = complicationPreviousPregnancy;
        this.lastMenstrualDate = lastMenstrualDate;
        this.expectedDateDelivery = expectedDateDelivery;
        this.expectedPlaceDelivery = expectedPlaceDelivery;
        this.JSY = JSY;
        this.PMMVY = PMMVY;
        this.delivery = delivery;
    }

    public String getPregnancyId() {
        return pregnancyId;
    }

    public void setPregnancyId(String pregnancyId) {
        this.pregnancyId = pregnancyId;
    }

    public int getPara() {
        return para;
    }

    public void setPara(int para) {
        this.para = para;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMotherId() {
        return momObjectID;
    }

    public void setMotherId(String motherId) {
        this.momObjectID = motherId;
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

    public com.hospitalmanagement.mainapp.GetterSetter.JSY getJSY() {
        return JSY;
    }

    public void setJSY(com.hospitalmanagement.mainapp.GetterSetter.JSY JSY) {
        this.JSY = JSY;
    }

    public com.hospitalmanagement.mainapp.GetterSetter.PMMVY getPMMVY() {
        return PMMVY;
    }

    public void setPMMVY(com.hospitalmanagement.mainapp.GetterSetter.PMMVY PMMVY) {
        this.PMMVY = PMMVY;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }
}
