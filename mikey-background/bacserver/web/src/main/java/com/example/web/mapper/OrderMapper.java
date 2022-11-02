package com.example.web.mapper;

import com.example.web.modules.Orders;
import com.example.web.modules.Want;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value = "OrderMapper")
public interface OrderMapper {

    void createOneOrder(int good_id, int user_id);
    List<Orders> selectOrderWithUser(String user_name);
    int lock(int good_id);
    int unlock(int good_id);
    List<Orders> getOrderCreatedCus(String user_name, Integer page, Integer size);
    List<Orders> getOrderCreatedBus(String user_name, Integer page, Integer size);
    List<Orders> getOrderDoneBus(String user_name, Integer page, Integer size);
    List<Orders> getOrderDoneCus(String user_name, Integer page, Integer size);
    Long getTotalCreatedBus(String user_name);
    Long getTotalCreatedCus(String user_name);
    Long getTotalDoneBus(String user_name);
    Long getTotalDoneCus(String user_name);
    void cancelOrderFirst(int order_id);
    void completeOrderFirst(int order_id);
    int cancelOrderSecond(int good_id);
    int completeOrderSecond(int good_id);

    //以下是基线需求购买商品所用代码
    void disableWants(int good_id);
    int chooseWants(int id);
    int createWant(String true_name, String phone_number, int id);
    int createWantSecond(int id);
    int completeWant(int id);
    int failWant(int id);
    List<Want> selectWantsNow(String user_name, Integer page, Integer size);
    List<Want> selectWantsDone(String user_name, Integer page, Integer size);
    List<Want> selectOrNow(String user_name, Integer page, Integer size);
    List<Want> selectOrDone(String user_name, Integer page, Integer size);

    //获取对应的数量
    Long selectWantsNowTotal(String user_name);
    Long selectWantsDoneTotal(String user_name);
    Long selectOrNowTotal(String user_name);
    Long selectOrDoneTotal(String user_name);
}
