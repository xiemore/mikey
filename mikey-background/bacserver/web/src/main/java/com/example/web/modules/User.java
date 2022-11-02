package com.example.web.modules;

import java.util.ArrayList;

public class User
{
    private int id;
    private String user_name;
    private String pwd;
    private String ni_name;
    private String phone_number;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getNi_name() {
        return ni_name;
    }
    public void setNi_name(String ni_name) {
        this.ni_name = ni_name;
    }
    public String getUser_name() {
        return user_name;
    }
    public void setUser_name(String username) {
        this.user_name = username;
    }
    public String getPwd() {
        return pwd;
    }
    public void setPwd(String password) {
        this.pwd = password;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
