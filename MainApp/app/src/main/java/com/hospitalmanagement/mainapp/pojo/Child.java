package com.hospitalmanagement.mainapp.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Child {

    @SerializedName("eligibleCoupleNameObjectId")
    @Expose
    private String eligibleCoupleNameObjectId;
    @SerializedName("childrenObjectId")
    @Expose
    private String childrenObjectId;
    @SerializedName("coupleid")
    @Expose
    private Integer coupleid;
    @SerializedName("childname")
    @Expose
    private String childname;
    @SerializedName("childRCH")
    @Expose
    private String childRCH;
    @SerializedName("vaccination")
    @Expose
    private Vaccination vaccination;

    public Integer getCoupleid() {
        return coupleid;
    }

    public void setCoupleid(Integer coupleid) {
        this.coupleid = coupleid;
    }

    public String getChildname() {
        return childname;
    }

    public void setChildname(String childname) {
        this.childname = childname;
    }

    public String getChildRCH() {
        return childRCH;
    }

    public void setChildRCH(String childRCH) {
        this.childRCH = childRCH;
    }

    public Vaccination getVaccination() {
        return vaccination;
    }

    public void setVaccination(Vaccination vaccination) {
        this.vaccination = vaccination;
    }

    public String getEligibleCoupleNameObjectId() {
        return eligibleCoupleNameObjectId;
    }

    public void setEligibleCoupleNameObjectId(String eligibleCoupleNameObjectId) {
        this.eligibleCoupleNameObjectId = eligibleCoupleNameObjectId;
    }

    public String getChildrenObjectId() {
        return childrenObjectId;
    }

    public void setChildrenObjectId(String childrenObjectId) {
        this.childrenObjectId = childrenObjectId;
    }
}