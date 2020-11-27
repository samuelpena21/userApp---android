package com.sapp.userapp.api.response;

import com.google.gson.annotations.SerializedName;
import com.sapp.userapp.api.models.Info;
import com.sapp.userapp.api.models.User;

import java.util.List;

public class UserResponse {
    @SerializedName("results")
    private List<User> results;

    @SerializedName("info")
    private Info info;

    public List<User> getResults() {
        return results;
    }

    public Info getInfo() {
        return info;
    }
}
