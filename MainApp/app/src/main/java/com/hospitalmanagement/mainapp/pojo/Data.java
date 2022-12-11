package com.hospitalmanagement.mainapp.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("getFamily")
    @Expose
    private GetFamily getFamily;
    @SerializedName("searchName")
    @Expose
    private List<SearchName> searchName = null;
    @SerializedName("updateMembers")
    @Expose
    private UpdateResponse updateMembers;
    @SerializedName("updateGeneralInfo")
    @Expose
    private UpdateResponse updateGeneralInfo;
    @SerializedName("updatePregnancy")
    @Expose
    private UpdateResponse updatePregnancy;
    @SerializedName("updateChildren")
    @Expose
    private UpdateResponse updateChildren;
    @SerializedName("updateEligibleCoupleName")
    @Expose
    private UpdateResponse updateEligibleCoupleName;

    @SerializedName("addMembers")
    @Expose
    private UpdateResponse  addMembers;




    @SerializedName("createFamily")
    @Expose
    private CreateFamily createFamily;

    public CreateFamily getCreateFamily() {
        return createFamily;
    }

    public void setCreateFamily(CreateFamily createFamily) {
        this.createFamily = createFamily;
    }





    public UpdateResponse getAddMembers() {
        return addMembers;
    }

    public void setAddMembers(UpdateResponse addMembers) {
        this.addMembers = addMembers;
    }

    public UpdateResponse getUpdateChildren() {
        return updateChildren;
    }

    public void setUpdateChildren(UpdateResponse updateChildren) {
        this.updateChildren = updateChildren;
    }

    public UpdateResponse getUpdateEligibleCoupleName() {
        return updateEligibleCoupleName;
    }

    public void setUpdateEligibleCoupleName(UpdateResponse updateEligibleCoupleName) {
        this.updateEligibleCoupleName = updateEligibleCoupleName;
    }

    public UpdateResponse getUpdatePregnancy() {
        return updatePregnancy;
    }

    public void setUpdatePregnancy(UpdateResponse updatePregnancy) {
        this.updatePregnancy = updatePregnancy;
    }

    public UpdateResponse getUpdateGeneralInfo() {
        return updateGeneralInfo;
    }

    public void setUpdateGeneralInfo(UpdateResponse updateGeneralInfo) {
        this.updateGeneralInfo = updateGeneralInfo;
    }

    public UpdateResponse getUpdateMembers() {
        return updateMembers;
    }

    public void setUpdateMembers(UpdateResponse updateMembers) {
        this.updateMembers = updateMembers;
    }

    public List<SearchName> getSearchName() {
        return searchName;
    }

    public void setSearchName(List<SearchName> searchName) {
        this.searchName = searchName;
    }

    public GetFamily getGetFamily() {
        return getFamily;
    }

    public void setGetFamily(GetFamily getFamily) {
        this.getFamily = getFamily;
    }

}