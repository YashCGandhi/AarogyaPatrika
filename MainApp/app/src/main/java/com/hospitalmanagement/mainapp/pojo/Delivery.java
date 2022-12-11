package com.hospitalmanagement.mainapp.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Delivery {

    @SerializedName("typeOfPregnancy")
    @Expose
    private String typeOfPregnancy;
    @SerializedName("outcomeOfPregnancy")
    @Expose
    private String outcomeOfPregnancy;

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