package com.sapp.userapp.api.models;

import java.io.Serializable;

public class Info implements Serializable {
    private String seed;
    private int results;
    private String page;

    public String getSeed() {
        return seed;
    }

    public int getResults() {
        return results;
    }

    public void setResults(int results) {
        this.results = results;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}
