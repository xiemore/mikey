package com.example.web.modules;

import java.util.List;
import java.util.Map;

public class Good {
    private int id;
    private String user_name;
    private String good_name;
    private int good_num;
    private String good_locker;
    private float good_price;
    private String good_log;
    private String good_pics;
    private String good_price_log;
    private int good_wants;

    public int getGood_wants() {
        return good_wants;
    }

    public void setGood_wants(int good_wants) {
        this.good_wants = good_wants;
    }

    private String down;

    public String getDown() {
        return down;
    }

    public void setDown(String down) {
        this.down = down;
    }

    public String getGood_pics() {
        return good_pics;
    }

    public void setGood_pics(String good_pics) {
        this.good_pics = good_pics;
    }

    public String getGood_price_log() {
        return good_price_log;
    }

    public void setGood_price_log(String good_price_log) {
        this.good_price_log = good_price_log;
    }

    private String good_type;

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

    private int good_type_id;
    private int user_name_id;

    public int getGood_type_id() {
        return good_type_id;
    }

    public void setGood_type_id(int good_type_id) {
        this.good_type_id = good_type_id;
    }

    public int getUser_name_id() {
        return user_name_id;
    }

    public void setUser_name_id(int user_name_id) {
        this.user_name_id = user_name_id;
    }
}
