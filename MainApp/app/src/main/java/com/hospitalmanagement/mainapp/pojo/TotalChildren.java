package com.hospitalmanagement.mainapp.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TotalChildren {

    @SerializedName("male")
    @Expose
    private Integer male;
    @SerializedName("female")
    @Expose
    private Integer female;
    @SerializedName("ageLastChild")
    @Expose
    private Integer ageLastChild;
    @SerializedName("genderLastChild")
    @Expose
    private String genderLastChild;

    public Integer getMale() {
        return male;
    }

    public void setMale(Integer male) {
        this.male = male;
    }

    public Integer getFemale() {
        return female;
    }

    public void setFemale(Integer female) {
        this.female = female;
    }

    public Integer getAgeLastChild() {
        return ageLastChild;
    }

    public void setAgeLastChild(Integer ageLastChild) {
        this.ageLastChild = ageLastChild;
    }

    public String getGenderLastChild() {
        return genderLastChild;
    }

    public void setGenderLastChild(String genderLastChild) {
        this.genderLastChild = genderLastChild;
    }

}