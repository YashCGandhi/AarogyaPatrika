package com.hospitalmanagement.mainapp.GetterSetter;

import java.util.ArrayList;

public class FamilyMemberList {
    private String membername;
    private int age;
    private String addhar_no;
    private String bank_acc;
    private String bank_name;
    private String mobile_no;
    private String sex;
    private ArrayList<String> disability_type;
    private ArrayList<String> disease;
    private ArrayList<String> modality;
    private String memberid;

    public FamilyMemberList(String name, int age, String aadharNo, String bankNo,
                            String bankName, String gender, ArrayList<String> disability,
                            ArrayList<String> disease, ArrayList<String> treatment,
                            String memberid, String mobile_no) {
        this.membername = name;
        this.age = age;
        this.addhar_no = aadharNo;
        this.bank_acc = bankNo;
        this.bank_name = bankName;
        this.sex = gender;
        this.disability_type = disability;
        this.disease = disease;
        this.modality = treatment;
        this.memberid = memberid;
        this.mobile_no = mobile_no;
    }

    public String getMemberid() {
        return memberid;
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }

    public String getName() {
        return membername;
    }

    public void setName(String name) {
        this.membername = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAadharNo() {
        return addhar_no;
    }

    public void setAadharNo(String aadharNo) {
        this.addhar_no = aadharNo;
    }

    public String getBankNo() {
        return bank_acc;
    }

    public void setBankNo(String bankNo) {
        this.bank_acc = bankNo;
    }

    public String getBankName() {
        return bank_name;
    }

    public void setBankName(String bankName) {
        this.bank_name = bankName;
    }

    public String getGender() {
        return sex;
    }

    public void setGender(String gender) {
        this.sex = gender;
    }

    public ArrayList<String> getDisability() {
        return disability_type;
    }

    public void setDisability(ArrayList<String> disability) {
        this.disability_type = disability;
    }

    public ArrayList<String> getDisease() {
        return disease;
    }

    public void setDisease(ArrayList<String> disease) {
        this.disease = disease;
    }

    public ArrayList<String> getTreatment() {
        return modality;
    }

    public void setTreatment(ArrayList<String> treatment) {
        this.modality = treatment;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }
}
