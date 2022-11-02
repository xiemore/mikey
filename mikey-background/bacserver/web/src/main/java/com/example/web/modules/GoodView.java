package com.example.web.modules;

import java.util.List;
import java.util.Map;

public class GoodView {
    private int id;
    private String user_name;
    private String good_name;
    private int good_num;
    private String good_locker;
    private float good_price;
    private String good_log;
    public List<String> good_pics;
    public List<Map<String, String>> good_price_log;
    private String good_type;
    private int good_wants;

    public int getGood_wants() {
        return good_wants;
    }

    public void setGood_wants(int good_wants) {
        this.good_wants = good_wants;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getGood_name() {
        return good_name;
    }

    public void setGood_name(String good_name) {
        this.good_name = good_name;
    }

    public int getGood_num() {
        return good_num;
    }

    public void setGood_num(int good_num) {
        this.good_num = good_num;
    }

    public String getGood_locker() {
        return good_locker;
    }

    public void setGood_locker(String good_locker) {
        this.good_locker = good_locker;
    }

    public float getGood_price() {
        return good_price;
    }

    public void setGood_price(float good_price) {
        this.good_price = good_price;
    }

    public String getGood_log() {
        return good_log;
    }

    public void setGood_log(String good_log) {
        this.good_log = good_log;
    }

    public String getGood_type() {
        return good_type;
    }

    public void setGood_type(String good_type) {
        this.good_type = good_type;
    }
}
