package com.hospitalmanagement.mainapp.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MutationQueryFamilyDetail {

    @SerializedName("query")
    @Expose
    private String query;
    @SerializedName("variables")
    @Expose
    private Variables variables;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Variables getVariables() {
        return variables;
    }

    public void setVariables(Variables variables) {
        this.variables = variables;
    }
}
