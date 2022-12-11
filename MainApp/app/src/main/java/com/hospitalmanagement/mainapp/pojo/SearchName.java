package com.hospitalmanagement.mainapp.pojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchName {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("members")
    @Expose
    private List<Member> members = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

}