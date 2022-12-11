package com.hospitalmanagement.mainapp.pojo;

public class CreateMemberPojo {
    private CreateMemberInput input;

    public CreateMemberPojo(CreateMemberInput input) {
        this.input = input;
    }

    public CreateMemberInput getInput() {
        return input;
    }

    public void setInput(CreateMemberInput input) {
        this.input = input;
    }
}
