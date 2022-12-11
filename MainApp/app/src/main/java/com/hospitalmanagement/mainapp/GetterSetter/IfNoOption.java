package com.hospitalmanagement.mainapp.GetterSetter;

public class IfNoOption {
    private String futureMethod;
    private String dateOfVisit;
    private String dateOfGroupMeeting;

    public IfNoOption(String futureMethod, String dateOfVisit, String dateOfGroupMeeting) {
        this.futureMethod = futureMethod;
        this.dateOfVisit = dateOfVisit;
        this.dateOfGroupMeeting = dateOfGroupMeeting;
    }

    public String getFutureMethod() {
        return futureMethod;
    }

    public void setFutureMethod(String futureMethod) {
        this.futureMethod = futureMethod;
    }

    public String getDateOfVisit() {
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
