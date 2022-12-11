package com.hospitalmanagement.mainapp.GetterSetter;

public class JSY {
    private int paidAmount;
    private String benefitDate;

    public JSY() {

    }

    public JSY(int paidAmount, String benefitDate) {
        this.paidAmount = paidAmount;
        this.benefitDate = benefitDate;
    }

    public int getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(int paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getBenefitDate() {
        return benefitDate;
    }

    public void setBenefitDate(String benefitDate) {
        this.benefitDate = benefitDate;
    }
}
