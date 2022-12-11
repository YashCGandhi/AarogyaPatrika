package com.hospitalmanagement.mainapp.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JSY {

    @SerializedName("paidAmount")
    @Expose
    private Object paidAmount;
    @SerializedName("benefitDate")
    @Expose
    private String benefitDate;

    public Object getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Object paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getBenefitDate() {
        return benefitDate;
    }

    public void setBenefitDate(String benefitDate) {
        this.benefitDate = benefitDate;
    }

}