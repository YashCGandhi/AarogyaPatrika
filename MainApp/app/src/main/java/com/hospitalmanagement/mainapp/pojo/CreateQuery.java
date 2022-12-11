package com.hospitalmanagement.mainapp.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateQuery {
    @SerializedName("query")
    @Expose
    private String query;
    @SerializedName("variables")
    @Expose
    private CreateMemberPojo variables;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public CreateMemberPojo getVariables() {
        return variables;
    }

    public void setVariables(CreateMemberPojo variables) {
        this.variables = variables;
    }
}
