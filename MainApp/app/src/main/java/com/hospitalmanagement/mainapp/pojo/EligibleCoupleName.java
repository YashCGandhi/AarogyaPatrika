package com.hospitalmanagement.mainapp.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EligibleCoupleName {

    @SerializedName("eligibleCoupleNameId")
    @Expose
    private String eligibleCoupleNameId;
    @SerializedName("husband")
    @Expose
    private String husband;
    @SerializedName("wife")
    @Expose
    private String wife;
    @SerializedName("coupleid")
    @Expose
    private Integer coupleid;
    @SerializedName("familyPlanningMethod")
    @Expose
    private String familyPlanningMethod;
    @SerializedName("totalChildren")
    @Expose
    private TotalChildren totalChildren;
    @SerializedName("ifNoOption")
    @Expose
    private IfNoOption ifNoOption;
    @SerializedName("fpmdates")
    @Expose
    private List<String> fpmdates;

    public List<String> getFpmdates() {
        return fpmdates;
    }

    public void setFpmdates(List<String> fpmdates) {
        this.fpmdates = fpmdates;
    }

    public String getHusband() {
        return husband;
    }

    public void setHusband(String husband) {
        this.husband = husband;
    }

    public String getWife() {
        return wife;
    }

    public void setWife(String wife) {
        this.wife = wife;
    }

    public Integer getCoupleid() {
        return coupleid;
    }

    public void setCoupleid(Integer coupleid) {
        this.coupleid = coupleid;
    }

    public String getFamilyPlanningMethod() {
        return familyPlanningMethod;
    }

    public void setFamilyPlanningMethod(String familyPlanningMethod) {
        this.familyPlanningMethod = familyPlanningMethod;
    }

    public TotalChildren getTotalChildren() {
        return totalChildren;
    }

    public void setTotalChildren(TotalChildren totalChildren) {
        this.totalChildren = totalChildren;
    }

    public IfNoOption getIfNoOption() {
        return ifNoOption;
    }

    public void setIfNoOption(IfNoOption ifNoOption) {
        this.ifNoOption = ifNoOption;
    }

    public String getEligibleCoupleNameId() {
        return eligibleCoupleNameId;
    }

    public void setEligibleCoupleNameId(String eligibleCoupleNameId) {
        this.eligibleCoupleNameId = eligibleCoupleNameId;
    }
}