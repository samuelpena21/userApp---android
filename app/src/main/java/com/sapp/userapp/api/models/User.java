package com.sapp.userapp.api.models;

import java.io.Serializable;

public class User implements Serializable {
    private Name name;
    private Picture picture;
    private String email;
    private String gender;
    private String phone;
    private String cell;

    public String getGender() {
        return gender;
    }

    public Name getName() {
        return name;
    }

    public Picture getPicture() {
        return picture;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getCell() {
        return cell;
    }

    public String getFullName(){
        return  name.toString();
    }
}
