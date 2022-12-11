package com.hospitalmanagement.mainapp.GetterSetter;

public class GeneralInfo {
    private boolean toilet;
    private boolean house;
    private String publicBoreHandpump;
    private String category;
    private boolean belowPovertyLine;

    public GeneralInfo(boolean toilet, boolean house, String publicBoreHandpump, String category, boolean belowPovertyLine) {
        this.toilet = toilet;
        this.house = house;
        this.publicBoreHandpump = publicBoreHandpump;
        this.category = category;
        this.belowPovertyLine = belowPovertyLine;
    }

    public boolean isToilet() {
        return toilet;
    }

    public void setToilet(boolean toilet) {
        this.toilet = toilet;
    }

    public boolean isHouse() {
        return house;
    }

    public void setHouse(boolean house) {
        this.house = house;
    }

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

    public boolean isBelowPovertyLine() {
        return belowPovertyLine;
    }

    public void setBelowPovertyLine(boolean belowPovertyLine) {
        this.belowPovertyLine = belowPovertyLine;
    }
}
