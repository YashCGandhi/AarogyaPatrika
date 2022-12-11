package com.hospitalmanagement.mainapp.pojo;

import java.math.BigInteger;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Vaccination {

    @SerializedName("DPTB_OPVB_MR2_VitA2")
    @Expose
    private List<String> dPTBOPVBMR2VitA2 = null;
    @SerializedName("MR1_VitA1")
    @Expose
    private List<String> mR1VitA1 = null;
    @SerializedName("OPV_3_IPV2_Penta3_Rota3")
    @Expose
    private List<String> oPV3IPV2Penta3Rota3 = null;
    @SerializedName("OPV_2_Penta_2_Rota_2")
    @Expose
    private List<String> oPV2Penta2Rota2 = null;
    @SerializedName("OPV1_IPV1_Penta1_Rota1")
    @Expose
    private List<String> oPV1IPV1Penta1Rota1 = null;
    @SerializedName("BCG")
    @Expose
    private List<String> bCG = null;
    @SerializedName("B2VIT")
    @Expose
    private List<String> b2VIT = null;
    @SerializedName("OPV")
    @Expose
    private List<String> oPV = null;

    public List<String> getDPTBOPVBMR2VitA2() {
        return dPTBOPVBMR2VitA2;
    }

    public void setDPTBOPVBMR2VitA2(List<String> dPTBOPVBMR2VitA2) {
        this.dPTBOPVBMR2VitA2 = dPTBOPVBMR2VitA2;
    }

    public List<String> getMR1VitA1() {
        return mR1VitA1;
    }

    public void setMR1VitA1(List<String> mR1VitA1) {
        this.mR1VitA1 = mR1VitA1;
    }

    public List<String> getOPV3IPV2Penta3Rota3() {
        return oPV3IPV2Penta3Rota3;
    }

    public void setOPV3IPV2Penta3Rota3(List<String> oPV3IPV2Penta3Rota3) {
        this.oPV3IPV2Penta3Rota3 = oPV3IPV2Penta3Rota3;
    }

    public List<String> getOPV2Penta2Rota2() {
        return oPV2Penta2Rota2;
    }

    public void setOPV2Penta2Rota2(List<String> oPV2Penta2Rota2) {
        this.oPV2Penta2Rota2 = oPV2Penta2Rota2;
    }

    public List<String> getOPV1IPV1Penta1Rota1() {
        return oPV1IPV1Penta1Rota1;
    }

    public void setOPV1IPV1Penta1Rota1(List<String> oPV1IPV1Penta1Rota1) {
        this.oPV1IPV1Penta1Rota1 = oPV1IPV1Penta1Rota1;
    }

    public List<String> getBCG() {
        return bCG;
    }

    public void setBCG(List<String> bCG) {
        this.bCG = bCG;
    }

    public List<String> getB2VIT() {
        return b2VIT;
    }

    public void setB2VIT(List<String> b2VIT) {
        this.b2VIT = b2VIT;
    }

    public List<String> getOPV() {
        return oPV;
    }

    public void setOPV(List<String> oPV) {
        this.oPV = oPV;
    }

}