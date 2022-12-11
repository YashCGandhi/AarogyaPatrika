package com.hospitalmanagement.mainapp.GetterSetter;

import java.util.ArrayList;

public class CoupleDetails {
    private String eligibleCoupleNameId;
    private String husband;
    private String husbandCoupleID;
    private String wife;
    private String wifeCoupleID;
    private int coupleid;
    private TotalChildren totalChildren;
    private String familyPlanningMethod;
    private IfNoOption ifNoOption;
    private ArrayList<String> fpmdates;

    public CoupleDetails() {
        
    }

    public CoupleDetails(String eligibleCoupleNameId, String husband, String husbandId,
                         String wife, String wifeId, int coupleid, String familyPlanningMethod) {
        this.eligibleCoupleNameId = eligibleCoupleNameId;
        this.husband = husband;
        this.husbandCoupleID = husbandId;
        this.wife = wife;
        this.wifeCoupleID = wifeId;
        this.coupleid = coupleid;
        this.familyPlanningMethod = familyPlanningMethod;
    }

    public CoupleDetails(String eligibleCoupleNameId, String husband, String husbandId, String wife, String wifeId, int coupleid, String familyPlanningMethod, IfNoOption ifNoOption) {
        this.eligibleCoupleNameId = eligibleCoupleNameId;
        this.husband = husband;
        this.husbandCoupleID = husbandId;
        this.wife = wife;
        this.wifeCoupleID = wifeId;
        this.coupleid = coupleid;
        this.familyPlanningMethod = familyPlanningMethod;
        this.ifNoOption = ifNoOption;
    }

    public CoupleDetails(String eligibleCoupleNameId, String husband, String husbandId, String wife, String wifeId, int coupleid, String familyPlanningMethod, ArrayList<String> fpmdates) {
        this.eligibleCoupleNameId = eligibleCoupleNameId;
        this.husband = husband;
        this.husbandCoupleID = husbandId;
        this.wife = wife;
        this.wifeCoupleID = wifeId;
        this.coupleid = coupleid;
        this.familyPlanningMethod = familyPlanningMethod;
        this.fpmdates = fpmdates;
    }

    public CoupleDetails(String eligibleCoupleNameId, String husband, String husbandId, String wife, String wifeId, int coupleid, TotalChildren totalChildren, String familyPlanningMethod) {
        this.eligibleCoupleNameId = eligibleCoupleNameId;
        this.husband = husband;
        this.husbandCoupleID = husbandId;
        this.wife = wife;
        this.wifeCoupleID = wifeId;
        this.coupleid = coupleid;
        this.totalChildren = totalChildren;
        this.familyPlanningMethod = familyPlanningMethod;
    }

    public CoupleDetails(String eligibleCoupleNameId, String husband, String husbandId, String wife, String wifeId, int coupleid, TotalChildren totalChildren, String familyPlanningMethod, IfNoOption ifNoOption) {
        this.eligibleCoupleNameId = eligibleCoupleNameId;
        this.husband = husband;
        this.husbandCoupleID = husbandId;
        this.wife = wife;
        this.wifeCoupleID = wifeId;
        this.coupleid = coupleid;
        this.totalChildren = totalChildren;
        this.familyPlanningMethod = familyPlanningMethod;
        this.ifNoOption = ifNoOption;
    }

    public CoupleDetails(String eligibleCoupleNameId, String husband, String husbandId, String wife, String wifeId, int coupleid, TotalChildren totalChildren, String familyPlanningMethod, ArrayList<String> fpmdates) {
        this.eligibleCoupleNameId = eligibleCoupleNameId;
        this.husband = husband;
        this.husbandCoupleID = husbandId;
        this.wife = wife;
        this.wifeCoupleID = wifeId;
        this.coupleid = coupleid;
        this.totalChildren = totalChildren;
        this.familyPlanningMethod = familyPlanningMethod;
        this.fpmdates = fpmdates;
    }

    public String getEligibleCoupleNameId() {
        return eligibleCoupleNameId;
    }

    public void setEligibleCoupleNameId(String eligibleCoupleNameId) {
        this.eligibleCoupleNameId = eligibleCoupleNameId;
    }

    public String getHusband() {
        return husband;
    }

    public void setHusband(String husband) {
        this.husband = husband;
    }

    public String getHusbandId() {
        return husbandCoupleID;
    }

    public void setHusbandId(String husbandId) {
        this.husbandCoupleID = husbandId;
    }

    public String getWife() {
        return wife;
    }

    public void setWife(String wife) {
        this.wife = wife;
    }

    public String getWifeId() {
        return wifeCoupleID;
    }

    public void setWifeId(String wifeId) {
        this.wifeCoupleID = wifeId;
    }

    public int getCoupleid() {
        return coupleid;
    }

    public void setCoupleid(int coupleid) {
        this.coupleid = coupleid;
    }

    public TotalChildren getTotalChildren() {
        return totalChildren;
    }

    public void setTotalChildren(TotalChildren totalChildren) {
        this.totalChildren = totalChildren;
    }

    public String getFamilyPlanningMethod() {
        return familyPlanningMethod;
    }

    public void setFamilyPlanningMethod(String familyPlanningMethod) {
        this.familyPlanningMethod = familyPlanningMethod;
    }

    public IfNoOption getIfNoOption() {
        return ifNoOption;
    }

    public void setIfNoOption(IfNoOption ifNoOption) {
        this.ifNoOption = ifNoOption;
    }

    public ArrayList<String> getFpmdates() {
        return fpmdates;
    }

    public void setFpmdates(ArrayList<String> fpmdates) {
        this.fpmdates = fpmdates;
    }
}
