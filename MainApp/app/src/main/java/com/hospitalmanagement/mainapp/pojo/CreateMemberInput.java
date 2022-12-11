package com.hospitalmanagement.mainapp.pojo;

import com.hospitalmanagement.mainapp.GetterSetter.ChildDetails;
import com.hospitalmanagement.mainapp.GetterSetter.CoupleDetails;
import com.hospitalmanagement.mainapp.GetterSetter.FamilyMemberList;
import com.hospitalmanagement.mainapp.GetterSetter.GeneralInfo;
import com.hospitalmanagement.mainapp.GetterSetter.PregnantDetails;

import java.util.ArrayList;

public class CreateMemberInput {
    private String ashaWorkerObjectid;
    private String areaname;
    private GeneralInfo general;
    private ArrayList<CoupleDetails> eligibleCoupleName;
    private ArrayList<ChildDetails> children;
    private ArrayList<PregnantDetails> pregnancy;
    private ArrayList<FamilyMemberList> members;

    public CreateMemberInput(String ashaWorkerObjectid, String areaname, GeneralInfo general, ArrayList<CoupleDetails> eligibleCoupleName, ArrayList<ChildDetails> children, ArrayList<PregnantDetails> pregnancy, ArrayList<FamilyMemberList> members) {
        this.ashaWorkerObjectid = ashaWorkerObjectid;
        this.areaname = areaname;
        this.general = general;
        this.eligibleCoupleName = eligibleCoupleName;
        this.children = children;
        this.pregnancy = pregnancy;
        this.members = members;
    }

    public String getAshaWorkerObjectid() {
        return ashaWorkerObjectid;
    }

    public void setAshaWorkerObjectid(String ashaWorkerObjectid) {
        this.ashaWorkerObjectid = ashaWorkerObjectid;
    }

    public String getAreaname() {
        return areaname;
    }

    public void setAreaname(String areaname) {
        this.areaname = areaname;
    }

    public GeneralInfo getGeneral() {
        return general;
    }

    public void setGeneral(GeneralInfo general) {
        this.general = general;
    }

    public ArrayList<CoupleDetails> getEligibleCoupleName() {
        return eligibleCoupleName;
    }

    public void setEligibleCoupleName(ArrayList<CoupleDetails> eligibleCoupleName) {
        this.eligibleCoupleName = eligibleCoupleName;
    }

    public ArrayList<ChildDetails> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<ChildDetails> children) {
        this.children = children;
    }

    public ArrayList<PregnantDetails> getPregnancy() {
        return pregnancy;
    }

    public void setPregnancy(ArrayList<PregnantDetails> pregnancy) {
        this.pregnancy = pregnancy;
    }

    public ArrayList<FamilyMemberList> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<FamilyMemberList> members) {
        this.members = members;
    }
}
