package com.example.web.modules;

//订单类
public class Orders {

    private int id;
    private int good_id;
    private String good_name;
    private String his_user_name;
    private String his_phone_number;

    public String getGood_name() {
        return good_name;
    }

    public void setGood_name(String good_name) {
        this.good_name = good_name;
    }

    private String bus_user_name;
    private String cus_user_name;
    private String bus_phone_number;
    private String cus_phone_number;
    private String order_stat;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBus_user_name() {
        return bus_user_name;
    }

    public void setBus_user_name(String bus_user_name) {
        this.bus_user_name = bus_user_name;
    }

    public String getCus_user_name() {
        return cus_user_name;
    }

    public void setCus_user_name(String cus_user_name) {
        this.cus_user_name = cus_user_name;
    }

    public String getBus_phone_number() {
        return bus_phone_number;
    }

    public void setBus_phone_number(String bus_phone_number) {
        this.bus_phone_number = bus_phone_number;
    }

    public String getCus_phone_number() {
        return cus_phone_number;
    }

    public void setCus_phone_number(String cus_phone_number) {
        this.cus_phone_number = cus_phone_number;
    }

    public String getOrder_stat() {
        return order_stat;
    }

    public void setOrder_stat(String order_stat) {
        this.order_stat = order_stat;
    }

    public int getGood_id() {
        return good_id;
    }

    public void setGood_id(int good_id) {
        this.good_id = good_id;
    }

    public String getHis_user_name() {
        return his_user_name;
    }

    public void setHis_user_name(String his_user_name) {
        this.his_user_name = his_user_name;
    }

    public String getHis_phone_number() {
        return his_phone_number;
    }

    public void setHis_phone_number(String his_phone_number) {
        this.his_phone_number = his_phone_number;
    }
}
