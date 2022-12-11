package com.hospitalmanagement.mainapp.GetterSetter;

public class ChildDetails {
    private String childrenObjectId;
    private String eligibleCoupleNameObjectId;
    private int coupleid;
    private String childname;
    private String childrenMemberId;
    private String childRCH;
    private Vaccination vaccination;

    public ChildDetails(String childrenObjectId, String eligibleCoupleNameObjectId, int coupleid,
                        String childname, String childId,String childRCH, Vaccination vaccination) {
        this.childrenObjectId = childrenObjectId;
        this.eligibleCoupleNameObjectId = eligibleCoupleNameObjectId;
        this.coupleid = coupleid;
        this.childname = childname;
        this.childrenMemberId = childId;
        this.childRCH = childRCH;
        this.vaccination = vaccination;
    }

    public String getChildId() {
        return childrenMemberId;
    }

    public void setChildId(String childId) {
        this.childrenMemberId = childId;
    }

    public String getChildrenObjectId() {
        return childrenObjectId;
    }

    public void setChildrenObjectId(String childrenObjectId) {
        this.childrenObjectId = childrenObjectId;
    }

    public String getEligibleCoupleNameObjectId() {
        return eligibleCoupleNameObjectId;
    }

    public void setEligibleCoupleNameObjectId(String eligibleCoupleNameObjectId) {
        this.eligibleCoupleNameObjectId = eligibleCoupleNameObjectId;
    }

    public int getCoupleid() {
        return coupleid;
    }

    public void setCoupleid(int coupleid) {
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
}
