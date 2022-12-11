package com.hospitalmanagement.mainapp.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class General {

//    @SerializedName("totalMembers")
//    @Expose
//    private Integer totalMembers;
    @SerializedName("toilet")
    @Expose
    private Boolean toilet;
    @SerializedName("house")
    @Expose
    private Boolean house;
//    @SerializedName("waterSupply")
//    @Expose
//    private Boolean waterSupply;
    @SerializedName("publicBoreHandpump")
    @Expose
    private String publicBoreHandpump;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("belowPovertyLine")
    @Expose
    private Boolean belowPovertyLine;

//    public Integer getTotalMembers() {
//        return totalMembers;
//    }
//
//    public void setTotalMembers(Integer totalMembers) {
//        this.totalMembers = totalMembers;
//    }

    public Boolean getToilet() {
        return toilet;
    }

    public void setToilet(Boolean toilet) {
        this.toilet = toilet;
    }

    public Boolean getHouse() {
        return house;
    }

    public void setHouse(Boolean house) {
        this.house = house;
    }

//    public Boolean getWaterSupply() {
//        return waterSupply;
//    }
//
//    public void setWaterSupply(Boolean waterSupply) {
//        this.waterSupply = waterSupply;
//    }

    public String getPublicBoreHandpump() {
        return publicBoreHandpump;
    }

    public void setPublicBoreHandpump(String publicBoreHandpump) {
        this.publicBoreHandpump = publicBoreHandpump;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getBelowPovertyLine() {
        return belowPovertyLine;
    }

    public void setBelowPovertyLine(Boolean belowPovertyLine) {
        this.belowPovertyLine = belowPovertyLine;
    }

}