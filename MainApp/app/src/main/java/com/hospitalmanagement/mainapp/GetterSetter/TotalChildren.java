package com.hospitalmanagement.mainapp.GetterSetter;

public class TotalChildren {
    int male;
    int female;
    int ageLastChild;
    String genderLastChild;

    public TotalChildren(int male, int female, int ageLastChild, String genderLastChild) {
        this.male = male;
        this.female = female;
        this.ageLastChild = ageLastChild;
        this.genderLastChild = genderLastChild;
    }

    public int getMale() {
        return male;
    }

    public void setMale(int male) {
        this.male = male;
    }

    public int getFemale() {
        return female;
    }

    public void setFemale(int female) {
        this.female = female;
    }

    public int getAgeLastChild() {
        return ageLastChild;
    }

    public void setAgeLastChild(int ageLastChild) {
        this.ageLastChild = ageLastChild;
    }

    public String getGenderLastChild() {
        return genderLastChild;
    }

    public void setGenderLastChild(String genderLastChild) {
        this.genderLastChild = genderLastChild;
    }
}
