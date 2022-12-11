package com.hospitalmanagement.mainapp.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;
import java.util.List;

public class GetFamily {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("mobileNo")
    @Expose
    private BigInteger mobileNo;
    @SerializedName("areaname")
    @Expose
    private String areaname;
    @SerializedName("general")
    @Expose
    private General general;
    @SerializedName("members")
    @Expose
    private List<Member> members = null;
    @SerializedName("eligibleCoupleName")
    @Expose
    private List<EligibleCoupleName> eligibleCoupleName = null;
    @SerializedName("children")
    @Expose
    private List<Child> children = null;
    @SerializedName("pregnancy")
    @Expose
    private List<Pregnancy> pregnancy = null;

    public List<Pregnancy> getPregnancy() {
        return pregnancy;
    }

    public void setPregnancy(List<Pregnancy> pregnancy) {
        this.pregnancy = pregnancy;
    }

    public List<Child> getChildren() {
        return children;
    }

    public void setChildren(List<Child> children) {
        this.children = children;
    }

    public List<EligibleCoupleName> getEligibleCoupleName() {
        return eligibleCoupleName;
    }

    public void setEligibleCoupleName(List<EligibleCoupleName> eligibleCoupleName) {
        this.eligibleCoupleName = eligibleCoupleName;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigInteger getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(BigInteger mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getAreaname() {
        return areaname;
    }

    public void setAreaname(String areaname) {
        this.areaname = areaname;
    }

    public General getGeneral() {
        return general;
    }

    public void setGeneral(General general) {
        this.general = general;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }
}