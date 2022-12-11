package com.hospitalmanagement.mainapp.pojo;

import java.math.BigInteger;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Member {


    @SerializedName("memberid")
    @Expose
    private String memberId;
    @SerializedName("membername")
    @Expose
    private String membername;
    @SerializedName("age")
    @Expose
    private Integer age;
    @SerializedName("sex")
    @Expose
    private String sex;
    @SerializedName("addhar_no")
    @Expose
    private String addharNo;
    @SerializedName("bank_acc")
    @Expose
    private String bankAcc;
    @SerializedName("bank_name")
    @Expose
    private String bankName;
    @SerializedName("disease")
    @Expose
    private List<String> disease = null;
    @SerializedName("disability_type")
    @Expose
    private List<String> disabilityType = null;
    @SerializedName("modality")
    @Expose
    private List<String> modality = null;
    @SerializedName("mobile_no")
    @Expose
    private String mobile_no;

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getMembername() {
        return membername;
    }

    public void setMembername(String membername) {
        this.membername = membername;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddharNo() {
        return addharNo;
    }

    public void setAddharNo(String addharNo) {
        this.addharNo = addharNo;
    }

    public String getBankAcc() {
        return bankAcc;
    }

    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public List<String> getDisease() {
        return disease;
    }

    public void setDisease(List<String> disease) {
        this.disease = disease;
    }

    public List<String> getDisabilityType() {
        return disabilityType;
    }

    public void setDisabilityType(List<String> disabilityType) {
        this.disabilityType = disabilityType;
    }

    public List<String> getModality() {
        return modality;
    }

    public void setModality(List<String> modality) {
        this.modality = modality;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
}