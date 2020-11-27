package com.sapp.userapp.api.models;

import java.io.Serializable;

public class Picture implements Serializable {
    private String large;
    private String medium;
    private String thumbnail;

    public String getLarge() {
        return large;
    }

    public String getMedium() {
        return medium;
    }

    public String getThumbnail() {
        return thumbnail;
    }

}
