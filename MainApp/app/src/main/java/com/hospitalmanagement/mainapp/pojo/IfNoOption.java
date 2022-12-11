package com.hospitalmanagement.mainapp.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IfNoOption {

    @SerializedName("futureMethod")
    @Expose
    private String futureMethod;
    @SerializedName("dateOfVisit")
    @Expose
    private String dateOfVisit;
    @SerializedName("dateOfGroupMeeting")
    @Expose
    private String dateOfGroupMeeting;

    public String getFutureMethod() {
        return futureMethod;
    }

    public void setFutureMethod(String futureMethod) {
        this.futureMethod = futureMethod;
    }

    public Object getDateOfVisit() {
        return dateOfVisit;
    }

    public void setDateOfVisit(String dateOfVisit) {
        this.dateOfVisit = dateOfVisit;
    }

    public String getDateOfGroupMeeting() {
        return dateOfGroupMeeting;
    }

    public void setDateOfGroupMeeting(String dateOfGroupMeeting) {
        this.dateOfGroupMeeting = dateOfGroupMeeting;
    }

}