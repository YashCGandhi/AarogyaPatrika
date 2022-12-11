package com.hospitalmanagement.mainapp.GetterSetter;

public class Delivery {
    private String typeOfPregnancy;
    private String outcomeOfPregnancy;

    public Delivery() {

    }

    public Delivery(String typeOfPregnancy, String outcomeOfPregnancy) {
        this.typeOfPregnancy = typeOfPregnancy;
        this.outcomeOfPregnancy = outcomeOfPregnancy;
    }

    public String getTypeOfPregnancy() {
        return typeOfPregnancy;
    }

    public void setTypeOfPregnancy(String typeOfPregnancy) {
        this.typeOfPregnancy = typeOfPregnancy;
    }

    public String getOutcomeOfPregnancy() {
        return outcomeOfPregnancy;
    }

    public void setOutcomeOfPregnancy(String outcomeOfPregnancy) {
        this.outcomeOfPregnancy = outcomeOfPregnancy;
    }
}
