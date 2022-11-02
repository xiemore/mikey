package com.example.web.modules;

//这是基线需求所需的实体类
public class Want {
    int id;

    String good_name;

    public String getGood_name() {
        return good_name;
    }

    public void setGood_name(String good_name) {
        this.good_name = good_name;
    }

    String true_name;
    String phone_number;
    int good_id;
    String is_chosen;
    String is_complete;

    public String getIs_complete() {
        return is_complete;
    }

    public void setIs_complete(String is_complete) {
        this.is_complete = is_complete;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTrue_name() {
        return true_name;
    }

    public void setTrue_name(String true_name) {
        this.true_name = true_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public int getGood_id() {
        return good_id;
    }

    public void setGood_id(int good_id) {
        this.good_id = good_id;
    }

    public String getIs_chosen() {
        return is_chosen;
    }

    public void setIs_chosen(String is_chosen) {
        this.is_chosen = is_chosen;
    }
}
